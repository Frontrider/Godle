@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


//vanilla godot with no modules
fun GodleExtension.godot(version: String): GodotVersion {
    return GodotVersion(
        os = os,
        bit = bit,
        version = version,
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/Godot_v%version%-stable_x11.%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/Godot_v%version%-stable_win%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/Godot_v%version%-stable_osx.universal.zip",
        linuxBinary = "Godot_v%version%-stable_x11.%bit%",
        windowsBinary = "Godot_v%version%-stable_win%bit%.exe",
        macBinary = "Godot_v%version%-stable_osx.universal"
    )
}

infix fun GodleExtension.asGodot(version: String) {
    this.version.set(godot(version))
}

infix fun GodleExtension.godot4Beta(version: String): GodotVersion {
    return GodotVersion(
        os = os,
        bit = bit,
        version = version,
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/4.0/beta%version%/Godot_v4.0-beta%version%_linux.x86_%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/4.0/beta%version%/Godot_v4.0-beta%version%winx%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/4.0/beta%version%/Godot_v4.0-beta%version%_macos.universal.zip",
        linuxBinary = "Godot_v4.0-beta%version%_linux.x86_%bit%",
        windowsBinary = "Godot_v4.0-beta%version%win%bit%.exe",
        macBinary = "Godot.app/Contents/MacOS/Godot",
        majorVersion = MajorVersion.Godot4
    )
}

infix fun GodleExtension.asGodot4Beta(version: String){
    this.version.set(godot4Beta(version))
}


//c# support
fun GodleExtension.mono(version: String): GodotVersion {
    return GodotVersion(
        os = os,
        bit = bit,
        version = version,
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_x11_%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_win%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_osx.universal.zip",
        linuxBinary = "/Godot_v%version%-stable_mono_x11_%bit%/Godot_v%version%-stable_mono_x11.%bit%",
        windowsBinary = "/Godot_v%version%-stable_mono_win%bit%/Godot_v%version%-stable_mono_win%bit%.exe",
        macBinary = "/Godot_v%version%-stable_mono_osx_universal/Godot_v%version%-stable_mono_osx.universal"
    )
}

infix fun GodleExtension.asMono(version: String) {
    this.version.set(mono(version))
}

var jvmPath:String? = null
//kotlin/jvm support
fun GodleExtension.`kotlin-jvm`(version: String, detectJVM: Boolean = true): GodotVersion {
    return GodotVersion(
        os = os,
        bit = bit,
        version = version,
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