@file:Suppress("unused", "UnusedReceiverParameter")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


//vanilla godot with no modules
fun GodleExtension.godot(version: String): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%",
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

//c# support
fun GodleExtension.mono(version: String): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "v%version%-%bit%-mono",
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_x11_%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_win%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/%version%/mono/Godot_v%version%-stable_mono_osx.universal.zip",
        linuxBinary = "/Godot_v%version%-stable_mono_x11_%bit%/Godot_v%version%-stable_mono_x11.%bit%",
        windowsBinary = "/Godot_v%version%-stable_mono_win%bit%/Godot_v%version%-stable_mono_win%bit%.exe",
        macBinary = "/Godot_v%version%-stable_mono_osx_universal/Godot_v%version%-stable_mono_osx.universal"
    )
}


fun GodleExtension.local(binaryPath: String,majorVersion: MajorVersion): GodotVersion {
    return GodotVersion(
        version = "local",
        cacheName = "local",
        linuxDownloadURL = "",
        windowsDownloadURL = "",
        macDownloadURL = "",
        linuxBinary = binaryPath,
        windowsBinary = binaryPath,
        macBinary = binaryPath,
        majorVersion = majorVersion
    )
}

fun GodleExtension.asLocal(binaryPath: String,majorVersion: MajorVersion){
    this.version.set(local(binaryPath,majorVersion))
}

//c# support
infix fun GodleExtension.asMono(version: String) {
    this.version.set(mono(version))
}
fun GodleExtension.godotBeta(majorVersion:String,version: String): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "godot-$majorVersion-beta-%version%-%bit%",
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/beta%version%/Godot_v$majorVersion-beta%version%_linux.x86_%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/beta%version%/Godot_v$majorVersion-beta%version%winx%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/beta%version%/Godot_v$majorVersion-beta%version%_macos.universal.zip",
        linuxBinary = "Godot_v$majorVersion-beta%version%_linux.x86_%bit%",
        windowsBinary = "Godot_v$majorVersion-beta%version%win%bit%.exe",
        macBinary = "Godot.app/Contents/MacOS/Godot",
        majorVersion = versionAsGodot4()
    )
}

fun GodleExtension.godotReleaseCandidate(majorVersion:String,version: String): GodotVersion {
    return GodotVersion(
        version = version,
        cacheName = "godot-$majorVersion-rc-%version%-%bit%",
        linuxDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/rc%version%/Godot_v$majorVersion-rc%version%_linux.x86_%bit%.zip",
        windowsDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/rc%version%/Godot_v$majorVersion-rc%version%winx%bit%.exe.zip",
        macDownloadURL = "https://downloads.tuxfamily.org/godotengine/$majorVersion/rc%version%/Godot_v$majorVersion-rc%version%_macos.universal.zip",
        linuxBinary = "Godot_v$majorVersion-rc%version%_linux.x86_%bit%",
        windowsBinary = "Godot_v$majorVersion-rc%version%win%bit%.exe",
        macBinary = "Godot.app/Contents/MacOS/Godot",
        majorVersion = versionAsGodot4()
    )
}