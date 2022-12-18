package io.github.frontrider.godle.tasks.exec

import io.github.frontrider.godle.initExec
import org.gradle.api.tasks.Exec

/**
 * executes the current godot binary.
 * */
@Suppress("LeakingThis")
open class GodotExec : Exec() {
    init {
        initExec(project, this)
        val task = this
        doLast{
            println("Godot task ran as: ")
            println("ran in: "+task.workingDir.absolutePath)
            println(commandLine)
            println(args)
        }
    }
}