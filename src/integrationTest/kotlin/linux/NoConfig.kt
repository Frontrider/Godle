package linux

import ScriptTypeArgumentProvider
import assertContainsGodotInformation
import assertExists
import createBuildFile
import doRun
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File

class NoConfig {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeArgumentProvider::class)
    fun `without any configuration, godot was downloaded`(postfix: String, @TempDir tempDir: File) {
        "noconfig".createBuildFile(postfix, tempDir)
        val result = doRun(tempDir, listOf("godotVersion"), "--stacktrace")
        val output = result.output
        assertAll(
            {
                output.assertContainsGodotInformation("3.5")
            },
            {
                File(tempDir.absolutePath + "/build/godle/temp/godot/Godot_V3.5_x11.64.zip").assertExists("download zip file is missing!")
            },
            {
                File(tempDir.absolutePath + "/build/godot/").assertExists("downloaded version folder missing!")
                println("files found in download folder:")
                File(tempDir.absolutePath + "/build/godot/").listFiles()?.forEach {
                    println(it.absolutePath)
                }
            },
            {
                File(tempDir.absolutePath + "/build/godot/").assertExists("godot cache folder missing!")
            },
            {
                File(tempDir.absolutePath + "/build/godot/Godot_v3.5-stable_x11.64").assertExists("executable is missing!")
            }, {
                Assertions.assertEquals(
                    TaskOutcome.SUCCESS,
                    result.task(":godotVersion")!!.outcome,
                    "Task godotVersion failed!"
                )
            },
            {
                Assertions.assertEquals(
                    TaskOutcome.SUCCESS,
                    result.task(":godotDownload")!!.outcome,
                    "Task godotDownload failed!"
                )
            }
        )
    }

}