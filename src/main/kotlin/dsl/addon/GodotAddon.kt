package io.github.frontrider.godle.dsl.addon

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.dsl.configureAsGodleInternal
import io.github.frontrider.godle.godleAddonsTaskName
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import java.io.File


/**
 * Declares a godot addon dependency
 * */
abstract class GodotAddon(val addonConfig: AddonConfig, val project: Project) {
    lateinit var targetFolder: String
    lateinit var downloadFolder: String
    lateinit var rootTaskName: String

    //a common root task that can depend on every other task.
    fun configure() {
        val extension = project.extensions.getByType(GodleExtension::class.java)
        val manageAddons = extension.manageAddons

        //do nothing if we're not managing the addons.
        if (!manageAddons) {
            return
        }

        project.afterEvaluate {
            targetFolder = project.buildDir.absolutePath + "/godle/addons/${getAddonInternalName()}"
            downloadFolder = project.buildDir.absolutePath + "/godle/temp/addon/${getAddonInternalName()}"
            rootTaskName = godleAddonsTaskName + getAddonInternalName()
            //the addon folder must be initialized here.
            addonConfig.sourceFolder = targetFolder

            val rootTask = project.tasks.create(rootTaskName) {
                it.configureAsGodleInternal()
            }

            project.tasks.getByName(godleAddonsTaskName).dependsOn(rootTask)

            init()
            addonConfig.init()
            createFolder(File(targetFolder))
            createFolder(File(downloadFolder))
            //This copy is the last task we do, everything else is set up to run before it.
            project.tasks.create("installGodotAddon${getAddonInternalName()}", Copy::class.java) {
                with(it) {
                    configureAsGodleInternal()
                    //uses the copyspec from the addon config.
                    with(addonConfig.copySpec)
                    destinationDir = File(extension.godotRoot.get().asFile.absolutePath + "/addons/")
                }
                rootTask.finalizedBy(it)
            }
        }
    }

    //this function is called to set up the dependency on the project.
    abstract fun init()

    /**
     * Returns the local folder where the addon's code can be found. It will be copied from there.
     */
    fun getLocalFolder(): File {
        return File(targetFolder)
    }

    fun createFolder(file: File) {
        if (!file.mkdirs()) {
            if (!file.exists()) {
                error("failed to create folder ${file.absolutePath}")
            }
        }
    }

    abstract fun getAddonInternalName(): String
}