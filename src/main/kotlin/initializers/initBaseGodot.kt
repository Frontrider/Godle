package io.github.frontrider.godle.initializers

import io.github.frontrider.godle.GodotFolder
import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.dsl.versioning.MajorVersion
import io.github.frontrider.godle.getGodotFolder
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

    afterEvaluate {
        val extension = extensions.getByName("godle") as GodleExtension
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

        val storePath = "$GodotFolder/"
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
                group = "godle-internal"
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

                destinationDir = File(getGodotFolder(extension.version.get()))
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

        when (version.majorVersion) {
            MajorVersion.Godot3 -> {
                tasks.create("godotGenerateBindingJson", GodotExec::class.java) { exec ->
                    with(exec) {
                        doFirst {
                            mkdir("${project.buildDir.absolutePath}/generated/godot/")
                        }
                        if (build != null) {
                            this.dependsOn(build)
                        }
                        description = "generate gdnative bindings"
                        group = "godle"

                        args("--gdnative-generate-json-api")
                        args("${project.buildDir.absolutePath}/generated/godot/api.json")
                        dependsOn(godotExtractTask)
                        dependsOn(godotDownloadTask)
                        dependsOn(godotAddonTask)
                    }
                }
            }

            MajorVersion.Godot4 -> {

                tasks.create("godotGenerateBindingJson", GodotExec::class.java) { exec ->
                    with(exec) {
                        workingDir = file("${project.buildDir.absolutePath}/generated/godot/")

                        doFirst {
                            mkdir(workingDir)
                        }
                        if (build != null) {
                            this.dependsOn(build)
                        }
                        description = "Generate gdextension bindings json"
                        group = "godle"

                        isIgnoreExitValue = true
                        args("--dump-extension-api")
                        args("--no-window")
                        dependsOn(godotExtractTask)
                        dependsOn(godotDownloadTask)
                        dependsOn(godotAddonTask)
                    }
                }

                tasks.create("godotGenerateGdExtensionInterface", GodotExec::class.java) { exec ->
                    with(exec) {
                        if (build != null) {
                            this.dependsOn(build)
                        }
                        workingDir = file("${project.buildDir.absolutePath}/generated/godot/")

                        doFirst {
                            mkdir(workingDir)
                        }
                        description = "Generate gdextension headers."
                        group = "godle"

                        isIgnoreExitValue = true

                        args("--dump-gdextension-interface")
                        args("--no-window")

                        dependsOn(godotExtractTask)
                        dependsOn(godotDownloadTask)
                        dependsOn(godotAddonTask)
                    }
                }
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