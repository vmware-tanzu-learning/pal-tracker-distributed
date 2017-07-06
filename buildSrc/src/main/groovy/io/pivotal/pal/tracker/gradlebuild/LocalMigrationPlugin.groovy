package io.pivotal.pal.tracker.gradlebuild

import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.task.FlywayCleanTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.flywaydb.gradle.task.FlywayRepairTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class LocalMigrationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.with {
            def databases = new DatabasesExtension()

            extensions.add("flyway", new FlywayExtension())
            extensions.add("databases", databases)

            afterEvaluate {
                addDbTask(project, "dev", databases.devDatabase)
                addDbTask(project, "test", databases.testDatabase)
            }
        }
    }

    private static addDbTask(Project project, String name, String dbName) {
        def flywayExtension = buildFlywayExtension(project, dbName)

        project.task("${name}Migrate", type: FlywayMigrateTask, group: "Migration") { extension = flywayExtension }
        project.task("${name}Clean", type: FlywayCleanTask, group: "Migration") { extension = flywayExtension }
        project.task("${name}Repair", type: FlywayRepairTask, group: "Migration") { extension = flywayExtension }
    }

    private static FlywayExtension buildFlywayExtension(Project project, String dbName) {
        def ext = new FlywayExtension()
        ext.with {
            url = "jdbc:mysql://localhost:3306/$dbName?useSSL=false&serverTimezone=UTC"
            user = "tracker"
            outOfOrder = false
            locations = ["filesystem:${project.projectDir}"]
        }
        return ext
    }
}
