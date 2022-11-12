package io.github.frontrider.godle.dsl.addon

import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.internal.file.copy.DefaultCopySpec
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@Suppress("Unused")
abstract class AddonConfig @Inject constructor(project: Project, objectFactory: ObjectFactory) {
    var sourceFolder = project.extensions.getByType(GodleExtension::class.java).godotRoot.asFile.get().absolutePath

    /**
     *When set to true, godle will treat the root folder of the addon as the root folder.
     *the format expected by godot means that there is an addon folder in the repository.
     */
    var isAddonAtRoot = false

    /**
     * This is to have special handling for projects downloaded from git repositories (github/gitlab etc).
     * */
    var isGitLike = false

    /**
     * Normally, the addon will only use the "addons" folder from the source.
     * This extra field can be used to add extra folders, like ones that store examples.
     */
    val copySpec: CopySpec = objectFactory.newInstance(DefaultCopySpec::class.java)

    fun init() {
        with(copySpec) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            includeEmptyDirs = false
            when {
                isAddonAtRoot && isGitLike -> {
                    copySpec.from(sourceFolder) {
                        include("/*/**")
                        eachFile {
                            it.path = it.sourcePath.split("addons")[1]
                        }

                    }
                }
                isAddonAtRoot -> {
                    copySpec.from(sourceFolder) {
                        eachFile {
                            println(it.path)
                        }
                    }
                }
                isGitLike -> {
                    copySpec.from(sourceFolder) {
                        include("/*/addons/**")
                        eachFile {
                            it.path = it.sourcePath.split("addons")[1]
                        }
                    }
                }

                else -> {
                    copySpec.from(sourceFolder) {
                        include("/addons/**")
                        eachFile {
                            it.path = it.sourcePath.split("addons")[1]
                        }
                    }
                }
            }
        }
    }
}