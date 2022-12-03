import io.github.frontrider.godle.dsl.versioning.asGodot

//We check that the plugin does not crash without setting anything.

plugins {
    id("io.github.frontrider.godle")
}
val godot_version:String by project

godle {
    asGodot(godot_version)
}