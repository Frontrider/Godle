package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.DefaultGodotVersion
import io.github.frontrider.godle.dsl.versioning.GodotVersion
import io.github.frontrider.godle.dsl.versioning.godot
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 *  the DSL to control the way the plugin interacts with Godot.
 * */
@Suppress("unused")
abstract class GodleExtension @Inject constructor(objectFactory: ObjectFactory, val project: Project) {

    val version: Property<GodotVersion> =
        objectFactory.property(GodotVersion::class.java).convention(godot(DefaultGodotVersion))

    //the root where the godot project lives. Defaults to the root folder.
    val godotRoot: RegularFileProperty = objectFactory.fileProperty().convention { project.projectDir }

    val godotHome: Property<String> = objectFactory.property(String::class.java).convention(
        when {
            (System.getenv("GODOT_HOME") != null) -> "${System.getenv("GODOT_HOME")}/"
            (System.getenv("GRADLE_HOME") != null) -> "${System.getenv("GRADLE_HOME")}/godle/"
            (System.getenv("HOME") != null) -> "${System.getenv("HOME")}/.gradle/godle/"
            else -> {
                "${System.getProperty("user.home")}/.gradle/godle/"
            }
        }
    )


    internal val godotCacheFolder: String
        get() {
            return "${godotHome.get()}/godot_cache"
        }

    internal val godotFolder: String
        get() {
            return "${godotHome.get()}/godot"
        }

    internal fun getGodotCache(version: GodotVersion): String {
        return "${godotCacheFolder}/${version.cachedName}/"
    }

    internal fun getGodotFolder(version: GodotVersion): String {
        return "$godotFolder/${version.cachedName}/"
    }

    fun getBindingJsonPath(): String {
        return project.buildDir.absolutePath + "/generated/godot/" + version.get().bindingName
    }

    fun getHeaderPath(): String {
        return project.buildDir.absolutePath + "/generated/godot/" + version.get().headerName
    }

    //set it to true, to ignore the build folder.
    var ignoreBuildFolder = true

    //set it to true, to create a blank godot project in the godot root.
    var createBlankProject = true

    /** When `true`, Godle will create several scripts to quickstart the Godot editor. */
    var generateQuickstartScripts = true

    val env = HashMap<String, String>()
    fun env(key: String, value: String) {
        env[key] = value
    }

}