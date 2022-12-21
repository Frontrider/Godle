package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.DefaultGodotVersion
import io.github.frontrider.godle.dsl.addon.GodotAddonExtension
import io.github.frontrider.godle.dsl.publishing.AddonPublishing
import io.github.frontrider.godle.dsl.testing.TestSettings
import io.github.frontrider.godle.dsl.versioning.GodotVersion
import io.github.frontrider.godle.dsl.versioning.godot
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
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

    var ignoreBuildFolder = true
    var createBlankProject = true

    //DSL for godot addons.
    @Nested
    abstract fun getAddons(): GodotAddonExtension

    open fun addons(action: Action<in GodotAddonExtension>) {
        action.execute(getAddons())
    }

    val env = HashMap<String, String>()
    fun env(key: String, value: String) {
        env[key] = value
    }
    @Nested
    abstract fun getPublishingConfig(): AddonPublishing

    internal var publishingEnabled = false
    fun publishing(action: Action<in AddonPublishing>){
        publishingEnabled = true
        action.execute(getPublishingConfig())
    }

}