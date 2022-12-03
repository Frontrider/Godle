package linux

import ScriptTypeAndVersionsArgumentProvider
import assertContainsGodotInformation
import assertExists
import createBuildFile
import doRun
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File

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
            },
            {
                File(tempDir.absolutePath + "/build/godle/temp/godot/godot.zip").assertExists("download zip file is missing!")
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
                File(tempDir.absolutePath + "/build/godot/Godot_v$version-stable_mono_x11_64/Godot_v$version-stable_mono_x11.64").assertExists("executable is missing!")
            }
        )
    }
}