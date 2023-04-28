package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.versioning.GodotVersion
import org.apache.commons.lang3.SystemUtils


//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_osx.universal.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win64.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win32.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.64.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.32.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_x11_.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_windows_.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_osx_.zip
//Default configuration values

internal const val DefaultGodotVersion = "3.5.1"
internal const val godleAddonsTaskName = "installGodotAddons"


//Global cache folders.
internal val GodotCacheFolder = when {
    (System.getenv("GODOT_HOME") != null) -> "${System.getenv("GODOT_HOME")}/godot_cache"
    (System.getenv("GRADLE_HOME") != null) -> "${System.getenv("GRADLE_HOME")}/godle/godot_cache"
    (System.getenv("HOME") != null) -> "${System.getenv("HOME")}/.gradle/godle/godot_cache"
    else -> {
        "${System.getProperty("user.home")}/.gradle/godle/godot_cache"
    }
}


internal val GodotFolder = when {
    (System.getenv("GODOT_HOME") != null) -> "${System.getenv("GODOT_HOME")}/godot"
    (System.getenv("GRADLE_HOME") != null) -> "${System.getenv("GRADLE_HOME")}/godle/godot"
    (System.getenv("HOME") != null) -> "${System.getenv("HOME")}/.gradle/godle/godot"
    else -> {
        "${System.getProperty("user.home")}/.gradle/godle/godot"
    }
}

internal fun getGodotCache(version: GodotVersion): String {
    return "$GodotCacheFolder/${version.cachedName}/"
}

internal fun getGodotFolder(version: GodotVersion): String {
    return "$GodotFolder/${version.cachedName}/"
}

//System type detection
enum class SUPPORTED_OS {
    LINUX, MAC, WINDOWS
}

val os = when {
    SystemUtils.IS_OS_MAC -> {
        SUPPORTED_OS.MAC
    }

    SystemUtils.IS_OS_WINDOWS -> {
        SUPPORTED_OS.WINDOWS
    }
    //we default to linux if we had no idea what the system is.
    else -> {
        SUPPORTED_OS.LINUX
    }
}
val bit = when (SystemUtils.OS_ARCH) {
    "x86" -> "32"
    else -> "64"
}