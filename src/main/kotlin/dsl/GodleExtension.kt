package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.DefaultGodotVersion
import io.github.frontrider.godle.GodotAssetStoreURL
import io.github.frontrider.godle.dsl.addon.GodotAddonDependencyContainer
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
    //set to true, if we want to enable and disable addons automatically.
    //this edits the project.godot file in the same folder as build.gradle
    var manageAddons: Boolean = true

    //set to true to include the companion gradle addon for godot, it includes a panel to manage gradle tasks.
    var enableGodotAddon: Boolean = false

    //if set to true, the plugin should gitignore the addons it downloaded.
    var enableAddonsGitignore: Boolean = false

    //if set to true, godot will remove all addons before downloading new ones.
    //by default, it is enabled together with addon management.
    var clearAddonsBeforeInstall = true

    var godotAssetStoreBaseURL: String = GodotAssetStoreURL

    val version: Property<GodotVersion> = objectFactory.property(GodotVersion::class.java).convention(godot(DefaultGodotVersion))

    //the root where the godot project lives. Defaults to the root folder.
    val godotRoot: RegularFileProperty = objectFactory.fileProperty().convention { project.rootDir }

    //DSL for godot addons.
    @Nested
    abstract fun getAddons(): GodotAddonDependencyContainer

    open fun addons(action: Action<in GodotAddonDependencyContainer>) {
        action.execute(getAddons())
    }

    val env = HashMap<String, String>()
    fun env(key: String, value: String) {
        env[key] = value
    }

}