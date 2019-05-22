package io.pivotal.pal.tracker.gradlebuild

import groovy.json.JsonSlurper
import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.flywaydb.gradle.task.FlywayRepairTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CfMigrationPlugin implements Plugin<Project> {
    private final static int TUNNEL_PORT = 63306
    private static final String KEY_NAME = 'flyway-migration-key'

    @Override
    void apply(Project project) {
        Process tunnelProcess = null
        Map credentials = null

        project.with {
            afterEvaluate {
                def databases = project.extensions.findByType(DatabasesExtension)
                def appName = databases.cfApp
                def databaseInstanceName = databases.cfDatabase

                task( "acquireCredentials") {
                    doLast {
                        println "Acquiring database credentials"
                        credentials = acquireMysqlCredentials(databaseInstanceName)
                    }
                }

                task("openTunnel") {
                    dependsOn "acquireCredentials"
                    doLast {
                        println "Opening Tunnel for $appName"
                        Thread.start {
                            tunnelProcess = "cf ssh -N -L ${TUNNEL_PORT}:${credentials['hostname']}:${credentials['port']} $appName".execute()
                        }

                        waitForTunnelConnectivity()
                    }
                }

                task("closeTunnel") {
                    doLast {
                        println "Closing Tunnel"
                        tunnelProcess?.destroyForcibly()
                    }
                }

                task("cfMigrate", type: FlywayMigrateTask, group: "Migration") {
                    dependsOn "openTunnel"
                    finalizedBy "closeTunnel"
                    doFirst { extension = buildFlywayExtension(project, credentials) }
                }

                task("cfRepair", type: FlywayRepairTask, group: "Migration") {
                    dependsOn "openTunnel"
                    finalizedBy "closeTunnel"
                    doFirst { extension = buildFlywayExtension(project, credentials) }
                }
            }
        }
    }

    private static void waitForTunnelConnectivity() {
        int remainingAttempts = 20
        while (remainingAttempts > 0) {
            remainingAttempts--
            try {
                new Socket('localhost', TUNNEL_PORT).close()
                remainingAttempts = 0
            } catch (ConnectException e) {
                println "Waiting for tunnel ($remainingAttempts attempts remaining)"
                sleep 1_000L
            }
        }
    }

    private static def buildFlywayExtension(Project project, Map credentials) {
        def extension = new FlywayExtension()

        extension.user = credentials['username']
        extension.password = credentials['password']
        def sslParam = ''
        switch (credentials['jdbcUrl']) {
            case ~/.*\buseSSL=false\b.*/:
                sslParam = '?useSSL=false'
                break
            case ~/.*\buseSSL=true\b.*/:
                sslParam = '?useSSL=true'
                break
        }
        extension.url = "jdbc:mysql://127.0.0.1:${TUNNEL_PORT}/${credentials['name']}${sslParam}"

        extension.locations = ["filesystem:$project.projectDir/migrations"]
        return extension
    }

    // Some services store their credentials in credhub, so they are
    // not available in VCAP_SERVICES seen by clients. Therefore, we
    // create a service key and then obtain the database credentials
    // from that value. Key creation appears idempotent, so there
    // is no need to check for prior existence.
    private static Map acquireMysqlCredentials(databaseInstanceName) {
        execute(['cf', 'create-service-key', databaseInstanceName, KEY_NAME])

        def serviceKeyJson = execute(['cf', 'service-key', databaseInstanceName, KEY_NAME])
                .replaceFirst(/(?s)^[^{]*/, '')

        return new JsonSlurper().parseText(serviceKeyJson) as Map
    }

    private static String execute(List args) {
        println "Executing command: ${args.join(' ')}"
        def process = args.execute()
        def output = process.text
        process.waitFor()

        println "Result of command: ${output}"
        return output
    }
}
