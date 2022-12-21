package linux

import ScriptTypeAndKotlinVersionsArgumentProvider
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
class KotlinEnabled {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeAndKotlinVersionsArgumentProvider::class)
    fun `With kotlin enabled`(postfix: String, version: String, @TempDir tempDir: File){
        "kotlinEnabled".createBuildFile(postfix, tempDir)
        doRun(tempDir, listOf("godotVersion"), "--stacktrace","-Pgodot_version=$version")
    }
}