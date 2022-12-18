package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.GodleExtension
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.Project
import org.gradle.process.ExecSpec

/**
 * Sets up an exec spec to run godot.
 * */
fun initExec(project: Project,exec: ExecSpec){
    with(exec) {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val storePath = "${project.buildDir.absolutePath}/$GodotFolder"

        val godotExecutable = storePath + "/" + extension.version.get().getBinaryPath()
        val workDir = extension.godotRoot.get().asFile
        workingDir(workDir)
        environment(extension.env)
        //--path argument is set to make sure that godot uses the right working directory.
        //it is redundant, but we pass it down anyways.
        if (SystemUtils.IS_OS_WINDOWS) {
            commandLine("cmd", "/c", godotExecutable)
        } else {
            commandLine(godotExecutable)
        }

        extension.version.get().execTask(this)
    }
}

fun Project.initExec(exec: ExecSpec,configure:ExecSpec.()->Unit){
    initExec(this,exec)
    exec.configure()
}