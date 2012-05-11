package org.jamescarr.gradle

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class H2Plugin implements Plugin <Project>{
	static final def H2_CONFIGURATION_NAME = "h2"


	static final def H2_START_TASK_NAME = "h2start"
	void apply(Project project){
		project.configurations.add(H2_CONFIGURATION_NAME).setVisible(false).setTransitive(true)
				.setDescription('The H2 library to be used for this project.')

		def convention = new H2PluginConvention()
		project.convention.plugins.h2 = convention

		configureH2Tasks(project,convention )
	}

	def configureH2Tasks(final Project project, final H2PluginConvention convention){
		project.tasks.withType(StartH2Task).whenTaskAdded { StartH2Task task ->
			project.gradle.taskGraph.whenReady {
				task.scripts = convention.scripts
				task.rootDir = project.buildFile.parentFile.absolutePath
			}
		}
		StartH2Task h2Start = project.tasks.add(H2_START_TASK_NAME, StartH2Task)
		h2Start.description = 'Starts an embedded h2 database.'
		h2Start.group = "h2"
	}
}