Plugin to manage godot dependencies through gradle.

Primarily meant to be used together with https://godot-kotl.in/, but it also supports several other binaries as well, and contains all the tools you need to take advantage of Gradle while using Godot.

The JVM module of godot is based on kotlin, so this plugin is also designed around the kotlin DSL, but is compatible with groovy.
The plugin itself needs java 9, godot kotlin requires 11.

# Installation

```kotlin
plugins {
    id("io.github.frontrider.godle") version "<godle version>"
}
```

# Configuration

```kotlin

configure<GodleExtension>{
    addons {
        //downloaded from the asset store
        byId("Metadata Inspector", 637)
        //downloaded from an url to a zip file
        byURL("https://github.com/Frontrider/Godot-Scene-Browser/archive/refs/heads/master.zip"){
            //zip files from git repos have a special structure that is handled by the plugin.
            //It will only copy whatever is inside the addons folder of the repository.
            isGitLike = true
            //a full copyspec is available for anything that needs more than that.
            copySpec
        }

        //godle comes with an optional godot addon that can be enabled. 
        //https://github.com/Frontrider/Godle-Godot-Addon/
        godleAddon()
    }
    
    //add a gitignore file into the addons folder, so it won't be committed into the repository.
    enableAddonsGitignore = true
    //set it to false, to avoid cleaning the addons folder
    cleanUndeclaredAddons.set(true)
    //you can set the url that the plugin should download assets from.
    //MUST point to an api compatible with godot's asset library!
    godotAssetStoreBaseURL = "https://godotengine.org/asset-library/api/"
    //configure the godot version of your choice.
    version = `kotlin-jvm`("0.5.1-3.5.1")
    
    //alternative kotlin handle for groovy.
    version = kotlinJvm("0.5.1-3.5.1")
    //the kotlin compat's second argument can disable automatic JVM detection.
    //it will first check the current runtime's jvm (as in the JVM that runs gradle right now), then the environment.
    `asKotlin-jvm`("0.5.1-3.5.1",false) 
    asGodot("3.5.1") 
    asMono("3.5.1") 
    //the folder where the godot project is located.
    godotRoot.set(project.rootDir)
    //environment variables for godot
    env["my_key"] = "my_value"
    env("my_key","my_value")
}

```
