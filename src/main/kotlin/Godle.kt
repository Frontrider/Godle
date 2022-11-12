package io.github.frontrider.godle

import fi.linuxbox.gradle.download.Download
import fi.linuxbox.gradle.download.DownloadPlugin
import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.tasks.GodotExec
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import java.io.File

class Godle : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("godle", GodleExtension::class.java)

        project.apply {
            it.plugin(DownloadPlugin::class.java)
        }
        val godotAddonTask = project.tasks.create(godleAddonsTaskName) {
            with(it) {
                description = "Donwload configured addons"
                group = "godle"
            }
        }

        project.afterEvaluate {
            val extension = it.extensions.getByName("godle") as GodleExtension
            println("Godot information:")
            println("Current version: ${extension.getDownloadConfig().godotVersion.get()}")
            val downloadConfig = extension.getDownloadConfig()

            val hasMono = downloadConfig.mono.get()
            val version = downloadConfig.godotVersion.get()
            val classifier = downloadConfig.classifier.get()
            val compressed = downloadConfig.isCompressed.get()
            val downloadPath = "${project.buildDir.absolutePath}/$GodotCacheFolder/"
            val storePath = "${project.buildDir.absolutePath}/$GodotFolder/"

            project.tasks.create("cleanGodotAddons", Delete::class.java) {
                with(it) {
                    delete(File(extension.godotRoot.asFile.get(),"addons"))
                    description = "Clean the addon folder. This is an optional step, you have to set up your task dependencies for this."
                    group = "godle"
                }
            }
            val godotDownloadTask = it.tasks.create("godotDownload", Download::class.java) { download ->
                with(download) {
                    description = "Downloads the configured godot version."
                    group = "godle"
                    from.set(extension.getDownloadURL())
                    to.set(
                        File(
                            if (hasMono) {
                                "${downloadPath}/Godot_mono_V${version}_$classifier"
                            } else {
                                "${downloadPath}/Godot_V${version}_$classifier"
                            } + if (compressed) {
                                ".zip"
                            } else {
                                ""
                            }
                        )
                    )
                    doFirst {
                        println("downloading godot from ${from.get()}")
                    }
                    //IF we already downloaded then this is up-to-date.
                    outputs.upToDateWhen {
                        to.get().asFile.exists()
                    }
                }
            }
            val godotExtractTask = it.tasks.create("godotExtract", Copy::class.java) { copy ->
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
                    if (compressed) {
                        from(it.zipTree(godotDownloadTask.to.get()))
                    } else {
                        from(godotDownloadTask.to.get())
                    }
                    destinationDir = File(storePath)
                }
            }

            it.tasks.create("godotEditor", GodotExec::class.java) { exec ->
                with(exec) {
                    description = "Launch the godot editor"
                    group = "godle"
                    args("--editor")

                    dependsOn(godotDownloadTask)
                    dependsOn(godotAddonTask)
                    dependsOn(godotExtractTask)
                }
            }

            it.tasks.create("godotRunGame", GodotExec::class.java) { exec ->
                with(exec) {
                    description = "Launch the game in the current project"
                    group = "application"
                    dependsOn(godotExtractTask)
                    dependsOn(godotDownloadTask)
                    dependsOn(godotAddonTask)
                }
            }

            it.tasks.create("godotVersion", GodotExec::class.java) { exec ->
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
}