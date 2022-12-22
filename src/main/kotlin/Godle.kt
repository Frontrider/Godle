package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.initializers.ignores
import io.github.frontrider.godle.initializers.initBaseGodot
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
class Godle : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("godle", GodleExtension::class.java)

        project.initBaseGodot()
        project.ignores()
    }
}
