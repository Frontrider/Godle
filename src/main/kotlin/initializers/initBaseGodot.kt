package io.github.frontrider.godle.initializers


import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.godleAddonsTaskName
import io.github.frontrider.godle.tasks.GodotDownload
import io.github.frontrider.godle.tasks.exec.GodotExec
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

import java.io.File

internal fun Project.initBaseGodot() {

    afterEvaluate {
        val extension = extensions.getByType(GodleExtension::class.java)
        println("Godot information:")
        val version = extension.version.get()
        println("Current version: ${version.version}")

        //If no project is found, create a blank one.
        if (extension.createBlankProject) {
            val file = File(extension.godotRoot.get().asFile, "project.godot")
            if (!file.exists()) {
                file.writeText("")
            }
        }
        //ignore the gradle wrapper's folder.
        if (File(project.projectDir, "gradle/").exists()) {
            val file = File(project.projectDir, "gradle/.gdignore")
            if (!file.exists()) {
                file.writeText("")
            }
        }
        //create runner scripts
        // Not in a task, to make it less cumbersome.
        File(project.projectDir, "editor.sh").apply {
            if (!exists()) {
                writeText(
                    "#!/bin/sh \n./gradlew godotEditor"
                )
            }
        }
        File(project.projectDir, "editor.bat").apply {
            if (!exists()) {
                writeText(
                    "gradlew.bat godotEditor"
                )
            }
        }
        File(project.projectDir, "game.sh").apply {
            if (!exists()) {
                writeText(
                    "#!/bin/sh \n./gradlew godotRunGame"
                )
            }
        }
        File(project.projectDir, "game.bat").apply {
            if (!exists()) {
                writeText("call gradlew.bat godotRunGame")
            }
        }

        val storePath = "${extension.godotFolder}/"
        val godotAddonTask = tasks.create(godleAddonsTaskName) {
            with(it) {
                description = "Download configured addons"
                group = "godle"
            }
        }
        val godotDownloadTask = tasks.create("godotDownload", GodotDownload::class.java) { download ->
            with(download) {
                to.set(File("${extension.getGodotCache(extension.version.get())}/godot.zip"))
                configureAsGodleInternal("Download the configured version of Godot.")
            }
        }
        val godotExtractTask = tasks.create("godotExtract", Copy::class.java) { copy ->
            with(copy) {

                configureAsGodleInternal("Copies the godot binary to its storage folder")
                enabled = godotDownloadTask.enabled
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

                destinationDir = File(extension.getGodotFolder(extension.version.get()))
            }
        }

        //IF a build task exists, then we depend on it.
        //The primary use of this is with Godot Kotlin/JVM, so the binaries are built and provided.
        //THIS WILL BE TREATED AS A CONVENTION! IF the build task exists, then it will run!
        val build = tasks.findByPath("build")

        tasks.create("godotRunEditor", GodotExec::class.java) { exec ->

            with(exec) {
                //IF a build task exists, then depend on it.
                //The primary use of this is with Godot Kotlin/JVM, so the binaries are built and provided to godot.
                //THIS WILL BE TREATED AS A CONVENTION! Anything running together with the build task, will be used!
                if (build != null) {
                    this.dependsOn(build)
                }

                configureAsGodleApplication("Launch the godot editor")

                args(version.majorVersion.editorFlag)
                args(version.majorVersion.projectPathFlag, extension.godotRoot.get().asFile.absolutePath)

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
                configureAsGodleApplication("Launch the game in the current project")

                args(version.majorVersion.projectPathFlag, extension.godotRoot.get().asFile.absolutePath)

                dependsOn(godotExtractTask)
                dependsOn(godotDownloadTask)
                dependsOn(godotAddonTask)
            }
        }

        tasks.create("godleGenerateBindings", GodotExec::class.java) { exec ->
            with(exec) {
                doFirst {
                    mkdir("${project.buildDir.absolutePath}/generated/godot/")
                }
                if (build != null) {
                    this.dependsOn(build)
                }
                workingDir("${project.buildDir.absolutePath}/generated/godot/")
                configureAsGodlePublic("Generate native bindings")
                args(version.majorVersion.headlessFlag)
                args(version.majorVersion.exportBindingsFlags)

                dependsOn(godotExtractTask)
                dependsOn(godotDownloadTask)
                dependsOn(godotAddonTask)
            }
        }


        tasks.create("godotVersion", GodotExec::class.java) { exec ->
            with(exec) {
                configureAsGodlePublic("Prints the version of the currently downloaded godot executable")
                args(version.majorVersion.versionFlag)
                dependsOn(godotDownloadTask)
                dependsOn(godotExtractTask)
            }
        }
    }
}

private fun Project.generateQuickstartScripts(extension: GodleExtension) {
    //ignore the gradle wrapper's folder.
    if (File(project.rootDir, "gradle/").exists()) {
        val file = File(project.rootDir, "gradle/.gdignore")
        if (!file.exists()) {
            file.writeText("")
        }
    }
    //create runner scripts
    // Not in a task, to make it less cumbersome.
    File(project.rootDir, "editor.sh").apply {
        if (!exists()) {
            writeText(
                "#!/bin/sh \n./gradlew godotEditor"
            )
        }
    }
    File(project.rootDir, "editor.bat").apply {
        if (!exists()) {
            writeText(
                "gradlew.bat godotEditor"
            )
        }
    }
    File(project.rootDir, "game.sh").apply {
        if (!exists()) {
            writeText(
                "#!/bin/sh \n./gradlew godotRunGame"
            )
        }
    }
    File(project.rootDir, "game.bat").apply {
        if (!exists()) {
            writeText("call gradlew.bat godotRunGame")
        }
    }
}