package io.github.frontrider.godle.tasks

import fi.linuxbox.gradle.download.Download
import io.github.frontrider.godle.GodotCacheFolder
import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.getGodotCache
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

/**
 * Downloads the godot binary.
 * */
@Suppress("LeakingThis")
abstract class GodotDownload @Inject constructor(workerExecutor: WorkerExecutor) :Download(workerExecutor) {

    init {
        val extension = project.extensions.getByName("godle") as GodleExtension
        val downloadPath = getGodotCache(extension.version.get())

        from.set(extension.version.get().getDownloadURL())
        println("downloading godot from: ${from.get()}")
        to.set(File(downloadPath,"godot.zip"))
        //IF we already downloaded then this is up-to-date.
        outputs.upToDateWhen {
            to.get().asFile.exists()
        }
        //post process the download task.
        extension.version.get().downloadTask(this)
    }
}