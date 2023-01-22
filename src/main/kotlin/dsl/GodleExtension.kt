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

    val version: Property<GodotVersion> = objectFactory.property(GodotVersion::class.java).convention(godot(DefaultGodotVersion))

    //the root where the godot project lives. Defaults to the root folder.
    val godotRoot: RegularFileProperty = objectFactory.fileProperty().convention { project.rootDir }

    fun getBindingJsonPath(): String {
        return project.buildDir.absolutePath+"/generated/godot/"+version.get().bindingName
    }

    fun getHeaderPath(): String {
        return project.buildDir.absolutePath+"/generated/godot/"+version.get().headerName
    }
    //set it to true, to ignore the build folder.
    var ignoreBuildFolder = true
    //set it to true, to create a blank godot project in the godot root.
    var createBlankProject = true

    val env = HashMap<String, String>()
    fun env(key: String, value: String) {
        env[key] = value
    }

}