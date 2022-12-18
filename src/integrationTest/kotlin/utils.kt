import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.AssertionFailureBuilder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.io.File
import java.util.*
import java.util.stream.Stream

fun String.assertContains(contains: String, message: String) {
    assertTrue(contains(contains), message)
}

fun String.assertContainsGodotInformation(expectedVersion: String) {
    assertContains("Godot information:", "Header not printed")
    assertContains("Current version: $expectedVersion", "Godot version ($expectedVersion) not printed/invalid!")
}

///tmp/junit5730095398451080741/build/godot/3.5/godot-3.5-x11_64.zip
fun File.assertExists(message: String) {
    println("asserting file path $absolutePath")
    if (!exists()) {
        AssertionFailureBuilder.assertionFailure()
            .message(message)
            .expected(absolutePath)
            .actual(absoluteFile.parentFile.walkTopDown().joinToString("\n"))
            .buildAndThrow()
    }
}

fun String.createBuildFile(postfix: String, tempDir: File) {
    val buildFile = File("${tempDir.absolutePath}/build.$postfix")
    val fileContents = TestRunner::class.java.getResource("$this/build.$postfix")!!.readText()
    buildFile.writeText(fileContents)
    println(fileContents)
}

fun doRun(projectDir: File, tasks: List<String>, vararg args: String, fail: Boolean = false): BuildResult {

    val builder = GradleRunner.create()
        .withProjectDir(projectDir)
        .withArguments(tasks + args.asList())
        .withPluginClasspath()

    val result = if (fail) builder.buildAndFail() else builder.build()

    val output = result.output
    print(output)
    return result
}

class ScriptTypeArgumentProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = LinkedList<Arguments>()
        for (postfix in postFixes) {
            arguments.add(Arguments.of(postfix))
        }
        return arguments.stream()
    }
}

class ScriptTypeAndVersionsArgumentProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = LinkedList<Arguments>()
        for (postfix in postFixes) {
            for (version in versions) {
                arguments.add(Arguments.of(postfix, version))
            }
        }
        return arguments.stream()
    }
}

class ScriptTypeAndKotlinVersionsArgumentProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val arguments = LinkedList<Arguments>()
        for (postfix in postFixes) {
            for (version in kotlinVersions) {
                arguments.add(Arguments.of(postfix, version))
            }
        }
        return arguments.stream()
    }
}


val postFixes = listOf("gradle.kts", "gradle")

//3.4 returns 255 as the error code when calling it with "--version", therefore it can not be supported properly. It will fail tests when it downloaded and ran successfully.
val versions = listOf("3.5", "3.5.1")

val kotlinVersions = listOf("0.5.0-3.5.0", "0.5.1-3.5.1")
