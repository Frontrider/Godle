//We check that the plugin does not crash without setting anything.
import io.github.frontrider.godle.dsl.versioning.`asKotlin-jvm`

plugins {
    id("io.github.frontrider.godle")
}
val godot_version:String by project

godle{
    `asKotlin-jvm`(godot_version)
}
