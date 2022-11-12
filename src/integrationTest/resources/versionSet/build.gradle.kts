import kotlin.reflect.full.memberFunctions

//We check that the plugin does not crash without setting anything.

plugins {
    id("io.github.frontrider.godle")
}
val godot_version:String by project

godle {
    downloadConfig {
        godotVersion.set(godot_version)
    }
}