package io.github.frontrider.godle.dsl.versioning

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.process.ExecSpec

fun setupJVM(detectJVM: Boolean = true, exec:ExecSpec) {
    //the exec task detects the current JVM
    if (detectJVM) {
        if (jvmPath != null) {
            exec.environment("JAVA_HOME", jvmPath!!)
            return
        }
        println("attempting to detect jvm!")

        //attempt to determine the current JVM's path and use it.
        val command = ProcessHandle.current()
            .info()
            .command()

        if (command.isPresent) {
            val javaHome = command.get().removeSuffix("/bin/java")
            println("jvm found at $javaHome")
            //determine from the process itself, this is the most secure variant
            exec.environment("JAVA_HOME", javaHome)
            jvmPath = javaHome
        } else {
            //determine from an environment variable, will most likely be set, but it may not be the correct one.
            val javaHome = System.getProperty("java.home")
            if (javaHome != null) {
                println("jvm found at $javaHome")
                exec.environment("JAVA_HOME", javaHome)
                jvmPath = javaHome
            }
        }
    }
}