package io.github.frontrider.godle.tasks.exec

import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import java.io.File
import javax.inject.Inject

/**
 * Executes a given GDScript file with the currently set godot version.
 * */
@Suppress("unused")
open class GodotScriptExec @Inject constructor() : GodotExec() {

    @InputFile
    var script: File? = null

    @Input
    var scriptArgs = ArrayList<String>()

    fun scriptArgs(vararg arg: String) {
        scriptArgs.addAll(arg)
    }

    @Input
    var headless = true


    init {
        doFirst {
            val extension = extensions.getByName("godle") as GodleExtension
            val version = extension.version.get().majorVersion

            args(version.scriptFlag, script!!.relativeTo(workingDir))
            if (headless) {
                args(version.headlessFlag)
            }
            args(scriptArgs)
        }
    }
}