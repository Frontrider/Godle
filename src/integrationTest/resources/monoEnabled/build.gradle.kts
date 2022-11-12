//We check that the plugin does not crash without setting anything.

plugins {
    id("io.github.frontrider.godle")
}
val godot_version:String by project

godle{
    downloadConfig{
        mono.set(true)
        godotVersion.set(godot_version)
    }
}
