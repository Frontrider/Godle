package linux

import ScriptTypeArgumentProvider
import assertContainsGodotInformation
import assertExists
import createBuildFile
import doRun
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File
@Disabled
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