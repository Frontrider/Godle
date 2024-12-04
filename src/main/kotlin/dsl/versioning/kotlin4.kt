@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


private val KOTLIN_JVM_MODULE_VERSIONS_TO_DETAILS =
    mapOf(
        // versions 0.10.0 and up all share the same schema
        SemanticVersion(0, 10, 0)..SemanticVersion.MAX to GodotVersionDetails(
            linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_linuxbsd_x86_64_release_%version%.zip",
            windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_x86_64_release_%version%.zip",
            macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_macos_universal_release_%version%.zip",
            linuxBinary = "godot.linuxbsd.editor.x86_%bit%.jvm.%kotlinJvmVersion%",
            windowsBinary = "godot.windows.editor.x86_%bit%.jvm.%kotlinJvmVersion%.exe",
            macBinary = "Godot",
        ),

        // versions 0.9.1 - 0.7.0 have a different pattern than newer versions
        SemanticVersion(0, 9, 1)..SemanticVersion(0, 7, 0) to GodotVersionDetails(
            linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_x11_%version%.zip",
            windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_%version%.zip",
            macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_osx_%version%.zip",
            linuxBinary = "godot.linuxbsd.editor.x86_%bit%",
            windowsBinary = "godot.windows.editor.x86_%bit%.exe",
            macBinary = "Godot",
        )

        // versions below 0.7.0 are not compatible with Godot 4.x.
    )




//kotlin/jvm support
fun GodleExtension.`kotlin-jvm4`(version: String, detectJVM: Boolean = true): GodotVersion {
    if (version.contains('-')) {
        // split the version of the godot engine and the godot-kotlin-jvm module
        // example input: 0.11.0-4.3
        val kotlinJvmModuleVersion = version.substringBefore('-')
        val semanticVersion = SemanticVersion.parseOrNull(kotlinJvmModuleVersion)
        if(semanticVersion == null){
            project.logger.debug("Could not parse semantic Godot-Kotlin-JVM module version from string '${kotlinJvmModuleVersion}'. Falling back to the defaults.")
            return createDefaultGodotVersion(version)
        }

        val details = KOTLIN_JVM_MODULE_VERSIONS_TO_DETAILS.asSequence().filter { semanticVersion in it.key }.maxByOrNull { it.key.start }?.value
        if(details == null){
            project.logger.debug("Could not determine a concrete version details for Godot-Kotlin-VM version '${kotlinJvmModuleVersion}'. Falling back to the defaults.")
            return createDefaultGodotVersion(version)
        }

        // we've determined the pattern for the download URLs for the given version.
        return createGodotVersionFromDetails(version, details)
    }

    // we could not parse the godot version into a semantic version for further differentiation
    // -> use the default format.
    return createDefaultGodotVersion(version)
}

private fun createGodotVersionFromDetails(version: String, details: GodotVersionDetails): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-kotlin",
        linuxDownloadURL = details.linuxDownloadURL,
        windowsDownloadURL = details.windowsDownloadURL,
        macDownloadURL = details.macDownloadURL,
        linuxBinary = details.linuxBinary,
        windowsBinary = details.windowsBinary,
        macBinary = details.macBinary,
        isJava = true,
        kotlinJvmVersion = version.substringBefore('-')
    )
}

private fun createDefaultGodotVersion(version: String): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-kotlin",
        linuxDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_x11_%version%.zip",
        windowsDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_windows_%version%.zip",
        macDownloadURL = "https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/%version%/godot-kotlin-jvm_editor_osx_%version%.zip",
        linuxBinary = "godot.linuxbsd.editor.x86_%bit%",
        windowsBinary = "godot.windows.editor.x86_%bit%.exe",
        macBinary = "Godot",
        isJava = true,
        kotlinJvmVersion = version.substringBefore('-')
    )
}

private data class GodotVersionDetails(
    val linuxDownloadURL: String,
    val windowsDownloadURL: String,
    val macDownloadURL: String,
    val linuxBinary: String,
    val windowsBinary: String,
    val macBinary: String,
)

/**
 * This variant exists for groovy.
 * */
fun GodleExtension.kotlinJvm4(version: String, detectJVM: Boolean = true): GodotVersion {
    return `kotlin-jvm4`(version, detectJVM)
}


infix fun GodleExtension.`asKotlin-jvm4`(version: String) {
    this.version.set(`kotlin-jvm4`(version))
}