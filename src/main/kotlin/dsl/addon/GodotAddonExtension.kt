package io.github.frontrider.godle.dsl.addon

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Nested
import javax.inject.Inject

/**
 * Configure addon handling.
 * */
@Suppress("unused")
abstract class GodotAddonExtension  @Inject constructor(
    val project: Project
)  {
    //if set to true, the plugin should gitignore the addons it downloaded.
    var enableAddonsGitignore: Boolean = false
    //if set to true, godot will remove all addons before downloading new ones.
    //by default, it is enabled together with addon management.
    var clearAddonsBeforeInstall = false

    //DSL for godot addons.
    @Nested
    abstract fun getInstalledAddons(): GodotAddonDependencyContainer

    open fun install(action: Action<in GodotAddonDependencyContainer>) {
        action.execute(getInstalledAddons())
    }

}