@file:Suppress("UnstableApiUsage")

package io.github.frontrider.godle.initializers

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.dsl.testing.TestEngines
import io.github.frontrider.godle.godleAddonsTaskName
import io.github.frontrider.godle.tasks.exec.GodotScriptExec
import io.github.frontrider.godle.tasks.testing.createExecutionSpec
import org.gradle.api.Project
import java.io.File

fun Project.initTesting() {
    project.afterEvaluate {
        val godotDownloadTask = tasks.named("godotDownload").get()
        val godotAddonTask = tasks.named(godleAddonsTaskName).get()
        val godotExtractTask = tasks.named("godotExtract").get()

        val extension = extensions.getByName("godle") as GodleExtension
        /*if (extension.testSettings.engine != TestEngines.NONE) {
            val executionSpec = createExecutionSpec(extension.testSettings)

            tasks.register("godotTest", GodotScriptExec::class.java) {
                with(it) {
                    script = File(project.rootDir,executionSpec.startScript)
                    scriptArgs += executionSpec.args
                    headless = true
                    group = "verification"
                    dependsOn(godotDownloadTask)
                    dependsOn(godotExtractTask)
                    dependsOn(godotAddonTask)
                }
            }
        }*/
    }
}
