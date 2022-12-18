package io.github.frontrider.godle.initializers

import io.github.frontrider.godle.GodotFolder
import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.godleAddonsTaskName
import io.github.frontrider.godle.tasks.GodotDownload
import io.github.frontrider.godle.tasks.exec.GodotExec
import org.gradle.api.Project
import org.gradle.api.attributes.TestSuiteType
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.testing.AggregateTestReport
import java.io.File

fun Project.initBaseGodot() {
    //configure the aggregator to merge the reports.
    extensions.getByType(ReportingExtension::class.java).apply {
        reports.create("testAggregateTestReport", AggregateTestReport::class.java) {
            it.testType.set(TestSuiteType.UNIT_TEST)
        }
    }
    //check should depend on the report merger.
    tasks.findByPath("check")?.dependsOn("testAggregateTestReport")

    afterEvaluate {
        val extension = extensions.getByName("godle") as GodleExtension
        println("Godot information:")
        println("Current version: ${extension.version.get().version}")

        val storePath = "${buildDir.absolutePath}/$GodotFolder/"
        val godotAddonTask = tasks.create(godleAddonsTaskName) {
            with(it) {
                description = "Download configured addons"
                group = "godle"
            }
        }

        val godotDownloadTask = tasks.create("godotDownload", GodotDownload::class.java) { download ->
            with(download) {
                description = "Downloads the configured godot version."
                group = "godle"
            }
        }
        val godotExtractTask = tasks.create("godotExtract", Copy::class.java) { copy ->
            with(copy) {
                description = "Copies the godot binary to its storage folder"
                group = "godle"
                //If the store exists, and is not empty then it is up-to-date.
                outputs.upToDateWhen {
                    val target = File(storePath)
                    if (target.exists()) {
                        return@upToDateWhen (target.listFiles()?.isNotEmpty()) ?: false
                    }
                    false
                }
                dependsOn.add(godotDownloadTask)

                from(zipTree(godotDownloadTask.to))
                fileMode = 500

                destinationDir = File(storePath)
            }
        }

        tasks.create("cleanGodotAddons", Delete::class.java) {
            with(it) {
                delete(
                    objects.fileProperty()
                        .convention { File(extension.godotRoot.asFile.get(), "addons") })
                description =
                    "Clean the addon folder. This is an optional step, you have to set up your task dependencies for this."
                group = "godle"
            }
            if (extension.getAddons().clearAddonsBeforeInstall) {
                godotAddonTask.dependsOn(it)
            }
        }

        //IF a build task exists, then we depend on it.
        //The primary use of this is with Godot Kotlin/JVM, so the binaries are built and provided.
        //THIS WILL BE TREATED AS A CONVENTION! IF the build task exists, then it will run!
        val build = tasks.findByPath("build")

        tasks.create("godotEditor", GodotExec::class.java) { exec ->

            with(exec) {
                //IF a build task exists, then depend on it.
                //The primary use of this is with Godot Kotlin/JVM, so the binaries are built and provided to godot.
                //THIS WILL BE TREATED AS A CONVENTION! Anything running together with the build task, will be used!
                if (build != null) {
                    this.dependsOn(build)
                }

                description = "Launch the godot editor"
                group = "godle"
                args("--editor")
                args("--path ${extension.godotRoot.get()}")

                dependsOn(godotDownloadTask)
                dependsOn(godotAddonTask)
                dependsOn(godotExtractTask)
            }
        }

        tasks.create("godotRunGame", GodotExec::class.java) { exec ->
            with(exec) {
                if (build != null) {
                    this.dependsOn(build)
                }
                description = "Launch the game in the current project"
                group = "application"

                args("--path ${extension.godotRoot.get()}")

                dependsOn(godotExtractTask)
                dependsOn(godotDownloadTask)
                dependsOn(godotAddonTask)
            }
        }

        tasks.create("godotVersion", GodotExec::class.java) { exec ->
            with(exec) {
                description = "Prints the version of the currently downloaded godot executable"
                group = "godle"
                args("--version")

                dependsOn(godotDownloadTask)
                dependsOn(godotExtractTask)
            }
        }
    }
}