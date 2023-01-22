package io.github.frontrider.godle.initializers

import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.api.Project
import java.io.File

/**
 * Set up gdignores on folders that need it.
 * */
internal fun Project.ignores(){
    afterEvaluate {

        val extension = extensions.getByName("godle") as GodleExtension

        //IF we don't want to ignore the build folder, then it won't do anything about it.
        if (extension.ignoreBuildFolder) {
            //ignore the build folder.
            fun ignoreBuild() {
                buildDir.mkdirs()
                File(buildDir, ".gdignore").createNewFile()
            }
            //make the clean task also finish by ignoring the build folder.
            //only apply it, if the clean task exists.
            ignoreBuild()
            tasks.findByPath("clean")?.apply {
                //If the clean task does not exist, then we don't ignore the build folder.
                doLast {
                    ignoreBuild()
                }
            }
        }
    }
}