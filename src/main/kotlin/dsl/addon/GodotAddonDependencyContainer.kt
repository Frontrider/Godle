package io.github.frontrider.godle.dsl.addon

import io.github.frontrider.godle.dsl.addon.sources.AssetStoreAddon
import io.github.frontrider.godle.dsl.addon.sources.FileAddon
import io.github.frontrider.godle.dsl.addon.sources.URLAddon
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * DSL to contain different kinds of Godot addon dependencies.
 *
 * Each addon can be declared with or without an addon config.
 * */
@Suppress("unused")
abstract class GodotAddonDependencyContainer @Inject constructor(
    val objectFactory: ObjectFactory,
    val project: Project
) {

    @Internal
    val dependencies = LinkedList<GodotAddon>()

    //creates a dependency from the assetlib
    //title is only used to make the api readable
    @Suppress("UNUSED_PARAMETER")
    fun byId(title: String, id: Int): GodotAddon {
        val assetStoreAddon =
            AssetStoreAddon(id.toString(), objectFactory.newInstance(AddonConfig::class.java), project)
        dependencies.add(assetStoreAddon)
        assetStoreAddon.configure()
        return assetStoreAddon
    }

    @Suppress("UNUSED_PARAMETER")
    fun byId(title: String, id: Int, action: AddonConfig.() -> Unit): GodotAddon {
        val addonConfig = objectFactory.newInstance(AddonConfig::class.java)
        addonConfig.action()
        val assetStoreAddon = AssetStoreAddon(id.toString(), addonConfig, project)
        dependencies.add(assetStoreAddon)
        assetStoreAddon.configure()

        return assetStoreAddon
    }

    //creates a dependency from a remote compressed file.
    fun byURL(fileURL: String): GodotAddon {
        val addonConfig = objectFactory.newInstance(AddonConfig::class.java)
        val urlAddon = URLAddon(fileURL, addonConfig, project)
        addonConfig.copySpec.from("")
        dependencies.add(urlAddon)
        urlAddon.configure()
        return urlAddon
    }

    fun byURL(fileURL: String, action: AddonConfig.() -> Unit): GodotAddon {
        val addonConfig = objectFactory.newInstance(AddonConfig::class.java)
        val urlAddon = URLAddon(fileURL, addonConfig, project)
        dependencies.add(urlAddon)
        addonConfig.action()
        urlAddon.configure()
        addonConfig.init()
        return urlAddon
    }

    //dependency from a local file, we allow both a string and a file as inputs.
    //this may not be useful, but is added to fulfill any arbitrary need we may have.
    fun byFile(file: File): GodotAddon {
        val addonConfig = objectFactory.newInstance(AddonConfig::class.java)
        val fileAddon=FileAddon(file, addonConfig, project)
        dependencies.add(fileAddon)
        fileAddon.configure()
        return fileAddon
    }

    fun byFile(file: File, action: AddonConfig.() -> Unit): GodotAddon {
        val addonConfig = objectFactory.newInstance(AddonConfig::class.java)
        val fileAddon=FileAddon(file, addonConfig, project)
        dependencies.add(fileAddon)
        addonConfig.action()
        fileAddon.configure()
        return fileAddon
    }

    fun byFile(file: String): GodotAddon {
        return byFile(File(file))
    }

    fun byFile(file: String, action: AddonConfig.() -> Unit): GodotAddon {
        return byFile(File(file), action)
    }

    //install the godle addon into godot.
    fun godleAddon(){
        byURL("https://github.com/Frontrider/Godle-Godot-Addon/archive/refs/heads/master.zip"){
            isGitLike = true
        }
    }
}