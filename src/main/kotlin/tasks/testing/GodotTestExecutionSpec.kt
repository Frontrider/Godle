package io.github.frontrider.godle.tasks.testing

import io.github.frontrider.godle.dsl.testing.TestEngines
import io.github.frontrider.godle.dsl.testing.TestSettings


fun createExecutionSpec(testSettings: TestSettings): GodotTestExecutionSpec {
    return when (testSettings.engine) {
        TestEngines.GUT3 -> GodotTestExecutionSpec(testSettings,
            "-gjunit_xml_file=",
            "gut3",
            "addons/gut/gut_cmdln.gd"
        )
        //this should not happen!
        else -> null!!
    }
}

class GodotTestExecutionSpec(
    testSettings: TestSettings,
    testResultArgument: String,
    name: String,
    val startScript: String
) {
    /**
     * Any additional CLI arguments.
     * */
    val args: ArrayList<String> = testSettings.args
    init {
        args.add("$testResultArgument./build/reports/junit/$name.xml")
    }
}
