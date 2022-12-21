package io.github.frontrider.godle

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.initializers.ignores
import io.github.frontrider.godle.initializers.initBaseGodot
import io.github.frontrider.godle.initializers.initTesting
import io.github.frontrider.godle.tasks.GodotDownload
import io.github.frontrider.godle.tasks.exec.GodotExec
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.TestSuiteType
import org.gradle.api.plugins.TestReportAggregationPlugin
import org.gradle.api.reporting.Reporting
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.testing.AggregateTestReport
import java.io.File

@Suppress("Unused")
class Godle : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("godle", GodleExtension::class.java)

        project.initBaseGodot()
        project.ignores()
        project.initTesting()
    }
}
