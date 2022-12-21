package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.versioning.GodotVersion


//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_osx.universal.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win64.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win32.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.64.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.32.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_x11_.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_windows_.zip
//https://github.com/utopia-rise/godot-kotlin-jvm/releases/download/0.5.1-3.5.1/godot-kotlin-jvm_editor_osx_.zip
//Default configuration values

const val DefaultGodotVersion = "3.5.1"
const val GodotAssetStoreURL = "https://godotengine.org/asset-library/api/"
const val TempFolder = "/godle/temp/"
val GodotCacheFolder = if(System.getenv("GODOT_HOME") != null) "${System.getenv("GODOT_HOME")}/godle/godot_cache" else "${System.getenv("HOME")}/.gradle/bin/godot_cache"
val GodotFolder = if(System.getenv("GODOT_HOME") != null) "${System.getenv("GODOT_HOME")}/godle/godot" else "${System.getenv("HOME")}/.gradle/bin/godot"

const val godleAddonsTaskName = "godleAddons"


fun getGodotCache(version:GodotVersion): String {
    return "$GodotCacheFolder/${version.cachedName}/"
}

fun getGodotFolder(version:GodotVersion): String {
    return "$GodotFolder/${version.cachedName}/"
}