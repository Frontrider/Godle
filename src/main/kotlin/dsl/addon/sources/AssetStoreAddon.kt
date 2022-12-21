package io.github.frontrider.godle.dsl.addon.sources

import fi.linuxbox.gradle.download.Download
import godot.assets.api.AssetsApi
import godot.assets.invoker.ApiException
import io.github.frontrider.godle.dsl.addon.AddonConfig
import io.github.frontrider.godle.dsl.addon.GodotAddon
import io.github.frontrider.godle.dsl.configureAsGodleInternal
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File

/**
 * Downloads an addon from the godot asset library.
 * */
class AssetStoreAddon(val id: String, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project) {
    override fun init() {
        val internalName = getAddonInternalName()
        try {
            val assetsApi = AssetsApi()
            val assetDetails = assetsApi.assetIdGet(id)
            //being gitlike is the default behavior, as most addons come from git repositories.

            addonConfig.isGitLike = assetDetails.downloadProvider!!.startsWith("Git")
            val download = project.tasks.create("downloadGodotAddonFromStore$internalName", Download::class.java) {
                with(it) {
                    configureAsGodleInternal()
                    from(assetDetails.downloadUrl)
                    to(File(downloadFolder, "store$internalName.zip"))
                }
            }

            val extract = project.tasks.create("extractGodotAddonFromStore$internalName", Copy::class.java) {
                with(it) {
                    configureAsGodleInternal()
                    from(project.zipTree(File(downloadFolder, "store$internalName.zip")))
                    into(getLocalFolder())
                    dependsOn(download)
                }
            }

            project.tasks.getByName(rootTaskName).dependsOn(extract)
        } catch (e: ApiException) {
            println("failed to resolve $id from godot asset library!")

            println(e.message)
            println("body:\n"+e.responseBody)
        }
    }

    override fun getAddonInternalName(): String {
        return "store-$id"
    }
}
