package io.github.frontrider.godle.dsl.versioning

data class MajorVersion(
    val editorFlag:String ="--editor",
    val projectPathFlag:String ="--path",
    val exportBindingsFlags:List<String> = listOf("--gdnative-generate-json-api"),
    val debugFlag:String = "--debug",
    val headlessFlag:String = "--no-windows",
    val versionFlag:String = "--version",
    val scriptFlag:String = "--script",
)

fun versionAsGodot3(): MajorVersion {
    return MajorVersion()
}
fun versionAsGodot4(): MajorVersion {
    return MajorVersion(
        exportBindingsFlags = listOf("--dump-gdextension-interface","--dump-extension-api"),
        headlessFlag = "--headless"
    )
}