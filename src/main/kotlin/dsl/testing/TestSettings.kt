package io.github.frontrider.godle.dsl.testing

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.internal.file.DefaultFilePropertyFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.language.base.internal.ProjectLayout
import java.io.File
import javax.inject.Inject

enum class TestEngines {
    GUT3,GDUnit3,NONE
}

open class TestSettings @Inject constructor(objectFactory: ObjectFactory,val project: Project) {
    /**
     * Selected test engine. "NONE" means testing is disabled
     * */
    var engine: TestEngines = TestEngines.NONE
    /**
     * additional command line arguments.
     * */
    val args = ArrayList<String>()

    fun args(vararg args: String) {
        this.args.addAll(args)
    }
}
