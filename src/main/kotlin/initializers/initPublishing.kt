package io.github.frontrider.godle.initializers

import io.github.frontrider.godle.dsl.GodleExtension
import io.github.frontrider.godle.tasks.GodotAddonPublish
import org.gradle.api.Project

fun Project.initPublishing() {

    afterEvaluate {
        val extension = extensions.getByName("godle") as GodleExtension
        //If publishing is disabled, then we don't do anything.
        if(extension.publishingEnabled){
            tasks.register("godotPublish",GodotAddonPublish::class.java){
                description = "Publish a Godot addon."
                group = "publishing"
            }
        }
    }

}