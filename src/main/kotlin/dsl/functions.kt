package io.github.frontrider.godle.dsl

import org.gradle.api.Task

fun Task.configureAsGodleInternal(){
    description ="godleInternalTask"
    group = "godle internal"
}