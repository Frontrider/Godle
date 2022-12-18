package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.DefaultGodotVersion
import io.github.frontrider.godle.dsl.addon.GodotAddonExtension
import io.github.frontrider.godle.dsl.testing.TestSettings
import io.github.frontrider.godle.dsl.versioning.GodotVersion
import io.github.frontrider.godle.dsl.versioning.godot
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import javax.inject.Inject

/**
 *  the DSL to control the way the plugin interacts with Godot.
 * */
@Suppress("unused")
abstract class GodleExtension @Inject constructor(objectFactory: ObjectFactory, val project: Project) {
    val version: Property<GodotVersion> = objectFactory.property(GodotVersion::class.java).convention(godot(DefaultGodotVersion))

    //the root where the godot project lives. Defaults to the root folder.
    val godotRoot: RegularFileProperty = objectFactory.fileProperty().convention { project.rootDir }

    val ignoreBuildFolder = true
    //DSL for godot addons.
    @Nested
    abstract fun getAddons(): GodotAddonExtension

    open fun addons(action: Action<in GodotAddonExtension>) {
        action.execute(getAddons())
    }
    /**
     * Test configuration. Done here, because it is not expected to have more than one test engines in the same project at the time.
     * It also must be static as task registration depends on it.
     * */
    //val testSettings = TestSettings(objectFactory, project)

    //open fun testing(action: Action<in TestSettings>) {
    //    action.execute(testSettings)
    //}

    val env = HashMap<String, String>()
    fun env(key: String, value: String) {
        env[key] = value
    }

}