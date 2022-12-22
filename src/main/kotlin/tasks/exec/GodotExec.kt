package io.github.frontrider.godle.tasks.exec

import io.github.frontrider.godle.initGodotExec
import org.gradle.api.tasks.Exec

/**
 * Executes the current godot binary, with given arguments.
 * */
@Suppress("LeakingThis")
open class GodotExec : Exec() {
    init {
        initGodotExec(project, this)
        val task = this
        doFirst{
            println("Godot task running as: ")
            println("runs in: "+task.workingDir.absolutePath)
            println(commandLine)
        }
    }
}