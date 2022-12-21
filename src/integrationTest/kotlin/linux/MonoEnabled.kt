package linux

import ScriptTypeAndVersionsArgumentProvider
import assertContainsGodotInformation
import assertExists
import createBuildFile
import doRun
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File

@Disabled
@EnabledOnOs(OS.LINUX)
class MonoEnabled {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeAndVersionsArgumentProvider::class)
    fun `With mono enabled`(postfix: String, version: String, @TempDir tempDir: File){
        "monoEnabled".createBuildFile(postfix, tempDir)
        val result = doRun(tempDir, listOf("godotVersion"), "--stacktrace","-Pgodot_version=$version")
        val output = result.output
        assertAll(
            {
                output.assertContainsGodotInformation(version)
            }
        )
    }
}