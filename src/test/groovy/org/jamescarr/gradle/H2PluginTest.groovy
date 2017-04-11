package org.jamescarr.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class H2PluginTest {
    private final File testDir = new File("build/tmp/tests")
    private Project project
    private H2Plugin plugin

    @Before
    void beforeEach() {
        project = ProjectBuilder.builder()
                .withProjectDir(testDir)
                .build()
        plugin = new H2Plugin().apply(project)
        //        project.convention.add "h2", {
        //            databaseName {
        //                scripts = ['a/b/c.sql']
        //            }
        //        }
    }

    @After
    void tearDown() {
        plugin = null

        if (testDir.exists()) {
            testDir.deleteDir()
        }
    }

    @Test
    void "verify task details"() {
        Task task = project.tasks[H2Plugin.H2_START_TASK_NAME]

        assert task.name == 'h2start'
        assert task.group == 'h2'
        assert task.description == 'Starts an embedded h2 database.'
    }

    @Test
    @Ignore("Under Construction")
    void "should have scripts available on task"() {
        println project.getState()

        Task task = project.tasks[H2Plugin.H2_START_TASK_NAME]

        assert task.scripts == ['databaseName': ['a/b/c.sql']]
    }


}
