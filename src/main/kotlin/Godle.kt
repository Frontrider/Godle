package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.tasks.GodotDownload
import io.github.frontrider.godle.tasks.GodotExec
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import java.io.File

@Suppress("Unused")
class Godle : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("godle", GodleExtension::class.java)

        project.afterEvaluate {


            val extension = project.extensions.getByName("godle") as GodleExtension
            val manageAddons = extension.manageAddons

            val godotAddonTask = project.tasks.create(godleAddonsTaskName) {
                with(it) {
                    description = if(manageAddons){
                        "Download configured addons"
                    }else{
                        "Download configured addons (disabled)"
                    }
                    enabled = manageAddons

                    group = "godle"
                }
            }
            println("Godot information:")
            println("Current version: ${extension.version.get().version}")

            val storePath = "${project.buildDir.absolutePath}/$GodotFolder/"

            val godotDownloadTask = project.tasks.create("godotDownload", GodotDownload::class.java) { download ->
                with(download) {
                    description = "Downloads the configured godot version."
                    group = "godle"
                }
            }
            val godotExtractTask = project.tasks.create("godotExtract", Copy::class.java) { copy ->
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

                    from(project.zipTree(godotDownloadTask.to))
                    fileMode = 500

                    destinationDir = File(storePath)
                }
            }

            if(manageAddons) {
                project.tasks.create("cleanGodotAddons", Delete::class.java) {
                    with(it) {
                        delete(
                            project.objects.fileProperty()
                                .convention { File(extension.godotRoot.asFile.get(), "addons") })
                        description =
                            "Clean the addon folder. This is an optional step, you have to set up your task dependencies for this."
                        group = "godle"
                    }
                    if(extension.clearAddonsBeforeInstall) {
                        godotAddonTask.dependsOn(this)
                    }
                }
            }


            //IF a build task exists, then we depend on it.
            //The primary use of this is with Godot Kotlin/JVM, so the binaries are built and provided.
            //THIS WILL BE TREATED AS A CONVENTION! IF the build task exists, then it will run!
            val build = project.tasks.findByPath("build")

            project.tasks.create("godotEditor", GodotExec::class.java) { exec ->

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

            project.tasks.create("godotRunGame", GodotExec::class.java) { exec ->
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

            project.tasks.create("godotVersion", GodotExec::class.java) { exec ->
                with(exec) {
                    description = "Prints the version of the currently downloaded godot executable"
                    group = "godle"
                    args("--version")

                    dependsOn(godotDownloadTask)
                    dependsOn(godotExtractTask)
                }
            }

            if(extension.enableAddonsGitignore) {
                File(project.rootDir, ".gitignore").writeText(
                    """
                *
                */
                !.gitignore
                """.trimIndent()
                )
            }
        }
    }
}
