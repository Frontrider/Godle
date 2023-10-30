@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


var jvmPath:String? = null
//kotlin/jvm support
fun GodleExtension.`kotlin-jvm3`(version: String, detectJVM: Boolean = true): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-kotlin",
        linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_x11_.zip",
        windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_.zip",
        macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_osx_.zip",
        linuxBinary = "godot.x11.opt.tools.%bit%",
        windowsBinary = "godot.windows.opt.tools.%bit%.exe",
        macBinary = "godot.osx.opt.tools.%bit%",
        isJava = true
    )
}

/**
 * This variant exists for groovy.
 * */
fun GodleExtension.kotlinJvm3(version: String, detectJVM: Boolean = true): GodotVersion {
    return `kotlin-jvm3`(version, detectJVM)
}


infix fun GodleExtension.`asKotlin-jvm3`(version: String) {
    this.version.set(`kotlin-jvm3`(version))
}