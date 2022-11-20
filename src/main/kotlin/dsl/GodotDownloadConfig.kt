package io.github.frontrider.godle.dsl

import io.github.frontrider.godle.*
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class GodotDownloadConfig @Inject constructor(objectFactory: ObjectFactory) {

    //the godot version the plugin should download. defaults to the value of "DefaultGodotVersion"
    val godotVersion: Property<String> = objectFactory.property(String::class.java).convention(DefaultGodotVersion)
    val godotDownloadBaseURL: Property<String> = objectFactory.property(String::class.java).convention(GodotDownloadBaseURL)
    val godotAssetStoreBaseURL: Property<String>  = objectFactory.property(String::class.java).convention(GodotAssetStoreURL)
    //set to true for the mono version of godot. defaults to false
    val mono: Property<Boolean>  = objectFactory.property(Boolean::class.java).convention(false)

    val classifier: Property<String> = objectFactory.property(String::class.java).convention(
        when {
            SystemUtils.IS_OS_MAC -> {
                ClassifierMac
            }
            SystemUtils.IS_OS_WINDOWS -> {
                when(SystemUtils.OS_ARCH){
                    "x86"->ClassifierWindows32
                    "amd64"->ClassifierWindows64
                    "ia64"->ClassifierWindows64
                    else-> ClassifierWindows32
                }
            }
            //we default to linux if we had no idea what the system is.
            else -> {
                when(SystemUtils.OS_ARCH){
                    "x86"->ClassifierLinux32
                    "amd64"->ClassifierLinux64
                    "ia64"->ClassifierLinux64
                    else-> ClassifierLinux32
                }
            }
        }
    )
    //If the full url needs to be overridden. IF set for the current platform, then the other configs are ignored.
    val linuxDownloadURL: Property<String>  = objectFactory.property(String::class.java).convention("")
    val windowsDownloadURL: Property<String>  = objectFactory.property(String::class.java).convention("")
    val macDownloadURL: Property<String>  = objectFactory.property(String::class.java).convention("")
    val isCompressed: Property<Boolean> = objectFactory.property(Boolean::class.java).convention(true)
}