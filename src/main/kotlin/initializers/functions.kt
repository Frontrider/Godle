package io.github.frontrider.godle.initializers

import org.gradle.api.Task

internal fun Task.configureAsGodleInternal(description:String="godleInternalTask"){
    this.description = description
    group = "godle internal"
}

internal fun Task.configureAsGodlePublic(description:String){
    this.description =description
    group = "godle"
}

internal fun Task.configureAsGodleApplication(description:String){
    this.description =description
    group = "application"
}