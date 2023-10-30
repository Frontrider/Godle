package io.github.frontrider.godle.tasks.exec

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.initGodotExec
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.internal.JavaToolchain

/**
 * Executes the current godot binary, with given arguments.
 * */
@Suppress("LeakingThis")
open class GodotExec : Exec() {

    @Input
    var debug = false

    init {
        initGodotExec(project, this)

        val extension = project.extensions.getByType(GodleExtension::class.java)
        val version = extension.version.get()

        if (version.isJava) {
            val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class.java)
            val toolchain = project.extensions.getByType(JavaToolchainService::class.java).compilerFor(javaPluginExtension.toolchain).get()
            val jvmPath = toolchain.metadata.installationPath
            environment("JAVA_HOME",jvmPath.asFile.absolutePath)
        }

        if (debug) {
            args(version.majorVersion.debugFlag)
        }
    }
}