package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.SUPPORTED_OS
import io.github.frontrider.godle.tasks.GodotDownload
import org.gradle.process.ExecSpec


/**
 * Contains all the information needed for downloads and execution.
 * Used as a common entry point for different installs with different engine modules.
 * */
data class GodotVersion(
    val linuxDownloadURL: String,
    val windowsDownloadURL: String,
    val macDownloadURL: String,

    val linuxBinary: String,
    val windowsBinary: String,
    val macBinary: String,

    val version: String,

    val os: SUPPORTED_OS = io.github.frontrider.godle.os,
    val bit: String = io.github.frontrider.godle.bit,
    val cacheName:String,

    val downloadTask: (GodotDownload) -> Unit = {},
    val execTask: (ExecSpec) -> Unit = {},
    val isJava:Boolean = false,
    val majorVersion: MajorVersion = versionAsGodot3(),
    val bindingName:String = "extension_api.json",
    val headerName:String = "gdextension_interface.h",

    val kotlinJvmVersion: String = "",
) {
    val cachedName:String = templateString(cacheName)
    fun templateString(input: String): String {
        return input
            .replace("%version%", version)
            .replace("%bit%", bit)
            // godot-kotlin-JVM versions 0.10.0-4.3.0 and higher append
            // the godot-kotlin-JVM version to the godot executable name (*argh*)
            // For non-kotlin projects, this replacement will have no effect because
            // the placeholder will not be present.
            .replace("%kotlinJvmVersion%", this.kotlinJvmVersion)
    }

    fun getDownloadURL(): String {
        return when (os) {
            SUPPORTED_OS.LINUX -> templateString(linuxDownloadURL)
            SUPPORTED_OS.MAC -> templateString(macDownloadURL)
            SUPPORTED_OS.WINDOWS -> templateString(windowsDownloadURL)
        }
    }

    fun getBinaryPath(): String {
        return when (os) {
            SUPPORTED_OS.LINUX -> templateString(linuxBinary)
            SUPPORTED_OS.MAC -> templateString(macBinary)
            SUPPORTED_OS.WINDOWS -> templateString(windowsBinary)
        }
    }
}
