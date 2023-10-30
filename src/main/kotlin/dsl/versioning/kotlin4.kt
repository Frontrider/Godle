@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension

//kotlin/jvm support
fun GodleExtension.`kotlin-jvm4`(version: String, detectJVM: Boolean = true): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-kotlin",
        linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_x11_%version%.zip",
        windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_%version%.zip",
        macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_osx_%version%.zip",
        linuxBinary = "godot.linuxbsd.editor.x86_%bit%",
        windowsBinary = "godot.windows.editor.x86_%bit%.exe",
        macBinary = "Godot",
        isJava = true
    )
}

/**
 * This variant exists for groovy.
 * */
fun GodleExtension.kotlinJvm4(version: String, detectJVM: Boolean = true): GodotVersion {
    return `kotlin-jvm4`(version, detectJVM)
}


infix fun GodleExtension.`asKotlin-jvm4`(version: String) {
    this.version.set(`kotlin-jvm4`(version))
}