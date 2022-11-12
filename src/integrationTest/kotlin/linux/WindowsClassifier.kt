package linux

import ScriptTypeArgumentProvider
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
class WindowsClassifier {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeArgumentProvider::class)
    fun `With classifier set to windows 64`(postfix: String, @TempDir tempDir: File){
        "classifierSet".createBuildFile(postfix, tempDir)
        val result = doRun(tempDir, listOf("godotExtract"), "--stacktrace")
        val output = result.output
        assertAll(
            {
                output.assertContainsGodotInformation("3.5")
            },
            {
                File(tempDir.absolutePath + "/build/godle/temp/godot/Godot_V3.5_win64.exe.zip").assertExists("download zip file is missing!")
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
                File(tempDir.absolutePath + "/build/godot/Godot_v3.5-stable_win64.exe").assertExists("executable is missing!")
            }
        )
    }

}