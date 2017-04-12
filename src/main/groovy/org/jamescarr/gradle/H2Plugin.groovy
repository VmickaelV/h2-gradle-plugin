package org.jamescarr.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.h2.tools.RunScript
import org.h2.tools.Server

import java.nio.charset.StandardCharsets

class H2Plugin implements Plugin<Project> {
    static final H2_CONFIGURATION_NAME = 'h2'
    static final H2_START_TASK_NAME = 'h2start'
    static final H2_STOP_TASK_NAME = 'h2stop'

    void apply(Project project) {
        project.extensions.create(H2_CONFIGURATION_NAME, H2PluginConvention)
        configureH2Tasks(project)
    }

    def configureH2Tasks(final Project project) {
        configureStartTask(project)
        configureStopTask(project)
    }


    private void configureStopTask(Project project) {
        project.task(H2_STOP_TASK_NAME) {
            description = 'Stops an embedded h2 database.'
            group = H2_CONFIGURATION_NAME
            doLast {
                org.h2.tools.Server.main("-tcpShutdown", "tcp://localhost:${project.h2.tcpPort}")
            }
        }
    }

    private void configureStartTask(Project project) {
        String rootDir = project.buildDir
        project.task(H2_START_TASK_NAME) {
            description = 'Starts an embedded h2 database.'
            group = H2_CONFIGURATION_NAME
            doLast {
                Server.main("-baseDir", rootDir, "-tcp", "-web", "-tcpPort", "${project.h2.ports.tcp}", "-webPort",
                        "${project.h2.ports.web}")
                project.h2.scripts.each { databaseName, scripts ->
                    new File("./${databaseName}.h2.db").delete()
                    project.h2.scripts.each { script ->
                        RunScript.execute("jdbc:h2:tcp://localhost:${project.h2.ports.tcp}/${databaseName}", "sa", "",
                                "${rootDir}/${script}", StandardCharsets.UTF_8, false)
                    }
                }
            }
        }
    }
}
