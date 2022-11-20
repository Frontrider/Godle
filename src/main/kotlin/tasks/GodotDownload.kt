package io.github.frontrider.godle.tasks

import fi.linuxbox.gradle.download.Download
import io.github.frontrider.godle.GodotCacheFolder
import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

abstract class GodotDownload @Inject constructor(workerExecutor: WorkerExecutor?) :Download(workerExecutor) {

    init {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val downloadConfig = extension.getDownloadConfig()

        val hasMono = downloadConfig.mono.get()
        val version = downloadConfig.godotVersion.get()
        val classifier = downloadConfig.classifier.get()
        val compressed = downloadConfig.isCompressed.get()
        val downloadPath = "${project.buildDir.absolutePath}/$GodotCacheFolder/"

        from.set(extension.getDownloadURL())
        to.set(
            File(
                if (hasMono) {
                    "${downloadPath}/Godot_mono_V${version}_$classifier"
                } else {
                    "${downloadPath}/Godot_V${version}_$classifier"
                } + if (compressed) {
                    ".zip"
                } else {
                    ""
                }
            )
        )
        //IF we already downloaded then this is up-to-date.
        outputs.upToDateWhen {
            to.get().asFile.exists()
        }

    }
}