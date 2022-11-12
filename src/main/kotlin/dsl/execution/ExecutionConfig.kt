package io.github.frontrider.godle.dsl.execution

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class ExecutionConfig @Inject constructor(objectFactory: ObjectFactory) {
    val env = HashMap<String,String>()
    fun env(key:String,value:String){
        env[key] = value
    }
    val binaryName = objectFactory.property(String::class.java)
}