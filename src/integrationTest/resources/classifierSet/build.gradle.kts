import kotlin.reflect.full.memberFunctions

//We check that the plugin does not crash without setting anything.

plugins {
    id("io.github.frontrider.godle")
}
val gradle_version:String by project

godle {
    downloadConfig {
        classifier.set("win64.exe")
    }
}