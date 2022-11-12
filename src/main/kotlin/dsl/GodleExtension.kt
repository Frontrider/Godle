package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.*
import io.github.frontrider.godle.dsl.addon.GodotAddonDependencyContainer
import io.github.frontrider.godle.dsl.execution.ExecutionConfig
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import java.io.File
import javax.inject.Inject

/**
 *  the DSL to control the way the plugin interacts with Godot.
 * */
abstract class GodleExtension @Inject constructor(objectFactory: ObjectFactory,val project: Project) {
    //set to true, if we want to enable and disable addons automatically.
    //this edits the project.godot file in the same folder as build.gradle
    val manageAddons: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(true)

    //set to true to include the companion gradle addon for godot, it includes a panel to manage gradle tasks.
    val enableGodotAddon: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(true)

    //if set to true, the plugin should gitignore the addons it downloaded.
    val enableAddonsGitignore: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(true)

    //if set to true, godot will remove any undeclared addons inside the addon folder.
    val cleanUndeclaredAddons: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(true)

    //the root where the godot project lives. Defaults to the root folder.
    val godotRoot: RegularFileProperty = objectFactory.fileProperty().convention { project.rootDir }

    @Nested
    abstract fun getDownloadConfig(): GodotDownloadConfig

    open fun downloadConfig(action: Action<in GodotDownloadConfig>) {
        action.execute(getDownloadConfig())
    }

    //DSL for godot addons.
    @Nested
    abstract fun getAddons(): GodotAddonDependencyContainer

    open fun addons(action: Action<in GodotAddonDependencyContainer>) {
        action.execute(getAddons())
    }

    //DSL for godot addons.
    @Nested
    abstract fun getExecution(): ExecutionConfig

    open fun execution(action: Action<in ExecutionConfig>) {
        action.execute(getExecution())
    }

    fun getDownloadURL(): String {

        val downloadConfig = getDownloadConfig()
        val version = downloadConfig.godotVersion.get()
        val baseUrl = downloadConfig.godotDownloadBaseURL.get()
        var classifier = downloadConfig.classifier.get()
        val hasMono = downloadConfig.mono.get()
        //a fix for the different linux classifiers between windows and linux.
        if (classifier == ClassifierLinux64 && hasMono) {
            classifier = ClassifierLinux64Mono
        }
        if (classifier == ClassifierLinux32 && hasMono) {
            classifier = ClassifierLinux32Mono
        }
        val downloadURL = downloadConfig.downloadURL.get()
        if (downloadURL.isNotEmpty()) {
            println("Download url set, getting godot from $downloadURL")
            return downloadURL
        }
        return if (hasMono) {
            monoUrlTemplate
        } else {
            baseUrlTemplate
        }.format(baseUrl, version, version, classifier)
    }
}