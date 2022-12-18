package linux.tests

import ScriptTypeAndKotlinVersionsArgumentProvider
import ScriptTypeArgumentProvider
import assertExists
import createBuildFile
import doRun
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File

@EnabledOnOs(OS.LINUX)
@Disabled
class Gut3 {
    @ParameterizedTest
    @ArgumentsSource(ScriptTypeArgumentProvider::class)
    fun `execute test task`(postfix: String, @TempDir tempDir: File) {
        "testRunner/gut3".createBuildFile(postfix, tempDir)

        val buildResult = doRun(tempDir, listOf("godotTest"), "--stacktrace", fail = false)
        assertAll(
            {
                File(tempDir.absolutePath + "/addons/gut/gut_cmdln.gd").assertExists("Gut CLI was not found.")
            })
    }
}