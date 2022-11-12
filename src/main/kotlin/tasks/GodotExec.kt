package io.github.frontrider.godle.tasks

import io.github.frontrider.godle.GodotFolder
import io.github.frontrider.godle.dsl.GodleExtension
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.tasks.Exec

open class GodotExec:Exec() {

    init {
        init()
    }
    private fun init() {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val downloadConfig = extension.getDownloadConfig()

        val hasMono = downloadConfig.mono.get()
        val version = downloadConfig.godotVersion.get()
        val classifier = downloadConfig.classifier.get()
        val storePath = "${project.buildDir.absolutePath}/$GodotFolder/"

        val godotExecutable = if(extension.getExecution().binaryName.isPresent){
            "$storePath${extension.getExecution().binaryName.get()}"
        }else{
            "$storePath${if(hasMono)"Godot_v$version-stable_mono_${classifier.replace(".","_")}/" else "/"}Godot_v$version-stable_${if(hasMono) "mono_" else ""}$classifier"
        }
        println("${name}: godot executable is at $godotExecutable")

        environment(extension.getExecution().env)
        if (SystemUtils.IS_OS_WINDOWS) {
            commandLine("cmd","/c",godotExecutable)
        } else {
            commandLine(godotExecutable)
        }
    }
}