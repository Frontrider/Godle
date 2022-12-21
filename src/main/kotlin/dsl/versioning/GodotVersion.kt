package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.SUPPORTED_OS
import io.github.frontrider.godle.tasks.GodotDownload
import org.gradle.process.ExecSpec

enum class MajorVersion {
    Godot3, Godot4
}



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

    val os: SUPPORTED_OS,
    val bit: String,
    val cacheName:String,

    val downloadTask: (GodotDownload) -> Unit = {},
    val execTask: (ExecSpec) -> Unit = {},
    val majorVersion: MajorVersion = MajorVersion.Godot3
) {
    val cachedName:String = templateString(cacheName)
    fun templateString(input: String): String {
        return input
            .replace("%version%", version)
            .replace("%bit%", bit)
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
