package io.github.frontrider.godle.dsl.addon.sources

import fi.linuxbox.gradle.download.Download
import io.github.frontrider.godle.dsl.addon.AddonConfig
import io.github.frontrider.godle.dsl.addon.GodotAddon
import io.github.frontrider.godle.dsl.configureAsGodleInternal
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File

class URLAddon(val url: String, addonConfig: AddonConfig, project: Project) : GodotAddon(addonConfig, project) {

    override fun init() {
        val internalName = getAddonInternalName()
        val download = project.tasks.create("downloadGodotAddonFromURL$internalName", Download::class.java) {
            with(it) {
                configureAsGodleInternal()
                from(url)
                to(File(downloadFolder,"url_$internalName.zip"))
            }
        }

        val extract = project.tasks.create("extractGodotAddonFromURL$internalName", Copy::class.java){
            with(it){
                configureAsGodleInternal()
                from(project.zipTree(File(downloadFolder,"url_$internalName.zip")))
                into(getLocalFolder())
                dependsOn(download)
            }
        }

        project.tasks.getByName(rootTaskName).dependsOn(extract)
    }

    override fun getAddonInternalName(): String {
        return url.replace("http://", "")
            .replace("https://", "")
            .replace("/", "_")
            .replace("#", "")
            .replace(Regex("\\?.*+"), "")
    }
}