@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


var jvmPath:String? = null
//kotlin/jvm support
fun GodleExtension.`kotlin-jvm`(version: String, detectJVM: Boolean = true): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-kotlin",
        linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_x11_.zip",
        windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_.zip",
        macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_osx_.zip",
        linuxBinary = "godot.x11.opt.tools.%bit%",
        windowsBinary = "godot.windows.opt.tools.%bit%.exe",
        macBinary = "godot.osx.opt.tools.%bit%",
        execTask = { exec ->
            //the exec task detects the current JVM
            if (detectJVM) {
                if(jvmPath != null){
                    exec.environment("JAVA_HOME", jvmPath!!)
                    return@GodotVersion
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
                    jvmPath= javaHome
                } else {
                    //determine from an environment variable, will most likely be set, but it may not be the correct one.
                    val javaHome = System.getProperty("java.home")
                    if (javaHome != null) {
                        println("jvm found at $javaHome")
                        exec.environment("JAVA_HOME", javaHome)
                        jvmPath= javaHome
                    }
                }
            }
        }
    )
}

/**
 * This variant exists for groovy.
 * */
fun GodleExtension.kotlinJvm(version: String, detectJVM: Boolean = true): GodotVersion {
    return `kotlin-jvm`(version, detectJVM)
}


infix fun GodleExtension.`asKotlin-jvm`(version: String) {
    this.version.set(`kotlin-jvm`(version))
}