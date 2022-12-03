package io.github.frontrider.godle.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import java.io.File
import javax.inject.Inject

/**
 * executes a given GDScript file with the current downloaded godot.
 * */
@Suppress("unused")
open class GodotScriptExec @Inject constructor() : GodotExec() {

    @InputFile
    var script: File? = null

    @Input
    var headless = true
    init {
        project.afterEvaluate {
            if(headless){
                args("--no-window")
            }
            args("--script", script!!.absolutePath)
        }
    }
}