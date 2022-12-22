![Godle Logo](![godle-logo.png](godle-logo.png))
# Godle, Godot plugin for Gradle

Plugin to manage Godot, and its dependencies through Gradle.

Primarily meant to be used together with https://godot-kotl.in/, but it also supports several other binaries as well, and contains all the tools you need to take advantage of Gradle while using Godot.

The JVM module of godot is based on kotlin, so this plugin is also designed around the kotlin DSL, but is compatible with groovy.

The plugin itself needs java 9 and gradle 7.4 ([uses the test report aggregation feature](https://docs.gradle.org/7.4/userguide/test_report_aggregation_plugin.html)), godot kotlin requires java 11.

# Installation

```kotlin
plugins {
    id("io.github.frontrider.godle") version "<godle version>"
}
```
https://plugins.gradle.org/plugin/io.github.frontrider.godle

[For more, read the wiki page of this repository.](https://github.com/Frontrider/Godle/wiki)