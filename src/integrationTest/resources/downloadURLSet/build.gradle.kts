//We check that the plugin does not crash without setting anything.

plugins {
    id("io.github.frontrider.godle")
}

godle {
    downloadConfig {
        windowsDownloadURL.set("https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win32.exe.zip")
        linuxDownloadURL.set( "https://downloads.tuxfamily.org/godotengine/3.5/Godot_v3.5-stable_win32.exe.zip")
    }
}