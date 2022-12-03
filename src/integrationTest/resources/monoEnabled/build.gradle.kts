//We check that the plugin does not crash without setting anything.
import io.github.frontrider.godle.dsl.versioning.asMono

plugins {
    id("io.github.frontrider.godle")
}
val godot_version:String by project

godle{
    asMono(godot_version)
}
