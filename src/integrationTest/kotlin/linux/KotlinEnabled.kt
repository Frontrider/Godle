package linux

import ScriptTypeAndKotlinVersionsArgumentProvider
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
class KotlinEnabled {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeAndKotlinVersionsArgumentProvider::class)
    fun `With kotlin enabled`(postfix: String, version: String, @TempDir tempDir: File){
        "kotlinEnabled".createBuildFile(postfix, tempDir)
        doRun(tempDir, listOf("godotVersion"), "--stacktrace","-Pgodot_version=$version")
        assertAll(
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
                File(tempDir.absolutePath + "/build/godot/godot.x11.opt.tools.64").assertExists("executable is missing!")
            }
        )
    }
}