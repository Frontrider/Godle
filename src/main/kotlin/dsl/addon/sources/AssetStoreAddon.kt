package io.github.frontrider.godle.dsl.addon.sources

import fi.linuxbox.gradle.download.Download
import io.github.frontrider.godle.dsl.addon.AddonConfig
import io.github.frontrider.godle.dsl.addon.GodotAddon
import org.gradle.api.Project
import java.io.File
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.dsl.configureAsGodleInternal
import kong.unirest.Unirest
import kong.unirest.UnirestException
import org.gradle.api.tasks.Copy

class AssetStoreAddon(val id: String, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project) {

    override fun init() {
        val internalName = getAddonInternalName()
        try {
            val extension = project.extensions.getByName("godle") as GodleExtension

            val assetStoreURL = extension.godotAssetStoreBaseURL

            val response = Unirest.get("$assetStoreURL/asset/$id").asString()
            val objectMapper = ObjectMapper().registerKotlinModule()
            //being gitlike is the default behavior, as most addons come from git repositories.

            val body = objectMapper.readValue(response.body, AssetStoreItem::class.java)
            addonConfig.isGitLike = body.downloadProvider.startsWith("Git")
            val download = project.tasks.create("downloadGodotAddonFromStore$internalName", Download::class.java) {
                with(it) {
                    configureAsGodleInternal()
                    from(body.downloadUrl)
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
        } catch (e: UnirestException) {
            println("failed to resolve $id from godot asset library!")
            println(e.cause?.message)
        }
    }

    override fun getAddonInternalName(): String {
        return "store-$id"
    }
}

data class AssetStoreItem(
    @JsonProperty("asset_id")
    val assetId: String,
    val type: String,
    val title: String,
    val author: String,
    @JsonProperty("author_id")
    val authorId: String,
    val version: String,
    @JsonProperty("version_string")
    val versionString: String,
    val category: String,
    @JsonProperty("category_id")
    val categoryId: String,
    @JsonProperty("godot_version")
    val godotVersion: String,
    val rating: String,
    val cost: String,
    val description: String,
    @JsonProperty("support_level")
    val supportLevel: String,
    @JsonProperty("download_provider")
    val downloadProvider: String,
    @JsonProperty("download_commit")
    val downloadCommit: String,
    @JsonProperty("browse_url")
    val browseUrl: String,
    @JsonProperty("issues_url")
    val issuesUrl: String,
    @JsonProperty("icon_url")
    val iconUrl: String,
    val searchable: String,
    @JsonProperty("modify_date")
    val modifyDate: String,
    @JsonProperty("download_url")
    val downloadUrl: String,
    val previews: List<Preview>,
    @JsonProperty("download_hash")
    val downloadHash: String,
)

data class Preview(
    @JsonProperty("preview_id")
    val previewId: String,
    val type: String,
    val link: String,
    val thumbnail: String,
)
