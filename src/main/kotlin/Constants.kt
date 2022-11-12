package io.github.frontrider.godle


//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_osx.universal.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win64.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win32.exe.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.64.zip
//https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_x11.32.zip
//Default configuration values

const val GodotDownloadBaseURL = "https://downloads.tuxfamily.org/godotengine/"
const val DefaultGodotVersion = "3.5"
const val GodotAssetStoreURL = "https://godotengine.org/asset-library/api/"
const val TempFolder = "/godle/temp/"
const val GodotCacheFolder = "$TempFolder/godot"
const val GodotFolder = "godot"

//classifiers
const val ClassifierMac = "universal"
const val ClassifierLinux64 = "x11.64"
const val ClassifierLinux64Mono = "x11_64"
const val ClassifierLinux32 = "x11.32"
const val ClassifierLinux32Mono = "x11_32"
const val ClassifierWindows64 = "win64.exe"
const val ClassifierWindows32 = "win32.exe"

//download urls
const val baseUrlTemplate = "%s/%s/Godot_v%s-stable_%s.zip"
const val monoUrlTemplate = "%s/%s/mono/Godot_v%s-stable_mono_%s.zip"

const val godleAddonsTaskName = "godleAddons"