import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.File


class TestRunner {

    @ParameterizedTest
    @ArgumentsSource(ScriptTypeArgumentProvider::class)
    fun `execute with no configuration`(postfix: String, @TempDir tempDir: File) {

        "noconfig".createBuildFile(postfix, tempDir)
        //run the tasks we want
        val result =doRun(tempDir, listOf("tasks", "buildEnvironment"), "--stacktrace")
        val output = result.output
        output.assertContains("Godot information:","Header not printed")
        output.assertContains("Current version: 3.5","Godot version not printed")
    }
}