package io.pivotal.pal.tracker.gradlebuild

import groovy.json.JsonSlurper
import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.flywaydb.gradle.task.FlywayRepairTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class CfMigrationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Process tunnelProcess = null

        project.with {
            afterEvaluate {
                def databases = project.extensions.findByType(DatabasesExtension)
                def appName = databases.cfApp

                task("openTunnel") {
                    doLast {
                        println "Opening Tunnel for $appName"
                        Thread.start {
                            tunnelProcess = "cf ssh -N -L 63306:${getMysqlHost(appName)}:3306 $appName".execute()
                        }
                        sleep 5_000L
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
                    doFirst { extension = buildFlywayExtension(project, appName) }
                }

                task("cfRepair", type: FlywayRepairTask, group: "Migration") {
                    dependsOn "openTunnel"
                    finalizedBy "closeTunnel"
                    doFirst { extension = buildFlywayExtension(project, appName) }
                }
            }
        }
    }


    private def getMysqlHost = { cfAppName ->
        return getMysqlCredentials(cfAppName)["hostname"]
    }

    private static def buildFlywayExtension(Project project, String cfAppName) {
        def extension = new FlywayExtension()

        getMysqlCredentials(cfAppName)?.with { credentials ->

            extension.user = credentials["username"]
            extension.password = credentials["password"]
            extension.url = "jdbc:mysql://127.0.0.1:63306/${credentials["name"]}"
        }

        extension.locations = ["filesystem:$project.projectDir/migrations"]
        return extension
    }

    private static def getMysqlCredentials(cfAppName) {
        def appGuid = execute("cf app $cfAppName --guid").trim()
        def envResponse = execute("cf curl /v2/apps/$appGuid/env")
        def envJson = new JsonSlurper().parseText(envResponse)
        def vcapServices = envJson["system_env_json"]?.getAt("VCAP_SERVICES")

        return vcapServices?.getAt("p-mysql")?.getAt(0)?.getAt("credentials")
    }

    private static String execute(String command) {
        def process = command.execute()
        def output = process.text
        process.waitFor()
        return output
    }
}
