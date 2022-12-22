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


# Configuration

```kotlin

configure<GodleExtension>{
    addons {
        //add a gitignore file into the addons folder, so it won't be committed into the repository.
        enableAddonsGitignore = true
        
        //when true, the addons folder will be cleaned.
        clearAddonsBeforeInstall.set(true)
        
        install {
            //downloaded from the asset store
            byId("Metadata Inspector", 637)
            //downloaded from an url to a zip file
            byURL("https://github.com/Frontrider/Godot-Scene-Browser/archive/refs/heads/master.zip") {
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
    }

    //creates a blank project in the godotRoot folder.
    createBlankProject = true
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

### C#
Godle could integrate one of the available gradle plugins to aid with dependency management for c# projects. 
https://plugins.gradle.org/search?term=dotnet

I don't make any promises for this, as I don't code in c# myself, but I see that as something that might be useful.

### Other languages
IF there is a gradle support for the language that you're writing your addon in, (like there is for kotlin/jvm at the moment) then it might be possible to include settings for that as well.

Godle can also try to support you either by doing the final steps like creating the `*.gdextension` file, or by organizing your external build tools.

IF it is possible to use your language's tools without installing them (they can be used in portable fashion), then godle can try to set you up for it. The benefit is that we can guarantee that anyone who works with your project has the right versions for your setup.

# Executing Godot.


## Godot binary

The `GodotExec` task can be used to run the currently downloaded godot binary with custom arguments. The plugin uses this class to run the editor and the game from gradle. 

```kotlin
tasks.create("godot_script", GodotExec::class.java) {
    args("your","arguments","to","godot")
}
```



## Script execution

Godot scripts can be executed with the `GodotScriptExec` task type.
This extends from `GodotExec`, provides a shorthand to run scripts.

```kotlin

tasks.create("godot_script",GodotScriptExec::class.java){
    //a file pointing at your script.
    script =file("<path_to_your_script>")
    //set it to false, to show godot's window
    headless = true
    //anything that works on a regular `Exec` task should work here as well. 
    group= "application"
}

```

# Publishing to the asset store

Godle will support addon publishing, but this feature is not available yet.

This is the currently planned configuration for it.
```kotlin
configure<GodleExtension> {
    //addon publishing won't be enabled until you call this method.
    publishing {
        //url to your git repository, the one you use from a browser, not the one ending in .git!
        vcsUrl.set("")
        
        category.set("")
        //the type of this asset.
        type = AddonType.ADDON
        //the download commit by default is always set to the last commit of the current git repository.
        //the plugin uses jgit to determine this.
        downloadCommit
        //The full description of your addon.
        var description: String = """
    The full description of your addon.
    """.trimIndent()
        //if download url is set, then we don't use the commit hash.
        downloadURL.set("")
        //godot has a list of licenses you can use, set to MIT by default!
        //godle will try to create the appropriate license files in the addons folder and the root folder.
        //the values are either objects inside the `godle.license` package, or `godle.license.License` instances.
        license = MIT
        //the version of godot the addon is compatible with
        godotVersion = CompatVersion.`3_5`
        //the asset's icon. This may 
        iconUrl.set("")
        //the default is vcsUrl/issues, link to your support page
        issuesUrl.set("")
        //support levels defined by the godot asset store, defaults to testing.
        supportLevel = SupportLevelEnum.TESTING
        //by default the addon's title is set to the project's name
        //yes, the project's name in gradle can be a fully human readable name, the example is in this project.
        title.set("")
        //by default the addon's version is set to the project's version
        versionString.set("")

        isArchived = false
        //your username and password that you use with the godot asset library
        //DO NOT STORE IT IN YOUR BUILDSCRIPT/PROJECT!
        credentials {
            username = ""
            password = ""
        }
    }
}

```

# Default Tasks

Godle provides a number of tasks to work with.

- `cleanGodotAddons`: cleans the addon folder, so godle can download it again.
- `godleAddons`: installs the currently configured addons
- `godotDownload`: downloads the currently configured version of Godot.
- `godotEditor`: runs the editor in the `godotRoot` folder (configured on the extension). It will open any projects in that folder.
- `godotVersion`: prints the current Godot version.
- `godotRunGame`: runs the game in the `godotRoot` folder (configured on the extension). It will open any projects in that folder.

Godle can produce gdnative/gdextension bindings for the currently downloaded godot version.

On Godot 3, use the `godotGenerateBindingJson` task to produce the json under `build/generated/godot/api.json`.

On Godot 4, use the `godotGenerateBindingJson` task to produce the json under `build/generated/godot/extension_api.json`, or the `godotGenerateGDExtensionInterface` to create godot's header file under `build/generated/godot/gdnative_interface.h`

This can be used to organize binding generation for that version of Godot.

<br>
<br>
<br>
Note for the gradle team for approval testing: the plugin without any configuration runs one of the later versions of godot 3.5, running the `godotEditor` task is enough to check that functionality, it should open the right version of the godot editor, with the created plank project skipping the project browser.