package io.github.frontrider.godle.tasks

import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.provider.DefaultProvider
import org.gradle.api.internal.provider.DefaultProviderFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

/**
 * Downloads the godot binary.
 * */
abstract class GodotDownload @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {

    @OutputFile
    val to: RegularFileProperty = objectFactory.fileProperty()

    @TaskAction
    fun download() {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val url = URL(extension.version.get().getDownloadURL())
        println("downloading godot from: $url")
        val file = to.get().asFile
        //this is an additional safeguard, do not remove it!
        //gradle is not supposed to run it if the output file exists, but we're making sure it won't if it is.
        //hacky fix for an issue I can't nail down yet-
        if(!file.exists()) {
            val path = file.toPath()
            url.openStream().use { Files.copy(it, path) }
        }
    }
}