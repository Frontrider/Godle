package io.github.frontrider.godle.tasks

import io.github.frontrider.godle.GodotFolder
import io.github.frontrider.godle.dsl.GodleExtension
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.tasks.Exec

/**
 * executes the current godot binary.
 * */
@Suppress("LeakingThis")
open class GodotExec : Exec() {

    init {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val storePath = "${project.buildDir.absolutePath}/$GodotFolder"

        val godotExecutable = storePath+"/"+extension.version.get().getBinaryPath()
        workingDir(extension.godotRoot.get())
        environment(extension.env)
        if (SystemUtils.IS_OS_WINDOWS) {
            commandLine("cmd", "/c",godotExecutable)
        } else {
            commandLine(godotExecutable)
        }
        extension.version.get().execTask(this)
    }
}