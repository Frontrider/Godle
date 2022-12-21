package io.github.frontrider.godle.tasks

import godot.assets.api.AssetsEditApi
import godot.assets.api.AuthApi
import godot.assets.model.AuthenticatedAssetDetails
import godot.assets.model.UsernamePassword
import io.github.frontrider.godle.dsl.GodleExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Publishes the project's addon to the store.
 * */
class GodotAddonPublish:DefaultTask() {

    @TaskAction
    fun publish(){
        val extension = extensions.getByName("godle") as GodleExtension

        val publishingConfig = extension.getPublishingConfig()
        val credentials = publishingConfig.credentials

        if(credentials.username.isEmpty()){
            error("Godot publishing username is empty, please set it. (DO NOT write it into the buildscript, store it externally!)")
        }
        if(credentials.password.isEmpty()){
            error("Godot publishing password is empty, please set it. (DO NOT write it into the buildscript, store it externally!)")
        }

        val authApi = AuthApi()
        val login = authApi.loginPost(UsernamePassword().apply {
            username = credentials.username
            password = credentials.password
        })

        if(login.token == null){
            error("Failed to authenticate with the Godot Asset Library, server did not return a token. Check your credentials, and try again!")
        }

        val assetsApi = AssetsEditApi()
        val assetDetails = AuthenticatedAssetDetails().apply {
            category = publishingConfig.category.get()
            description = publishingConfig.description
            cost = publishingConfig.license.licenseId
            if(publishingConfig.downloadURL.isPresent){
                downloadUrl = publishingConfig.downloadURL.get()
            }else{
                //download commit is always available
                downloadCommit = publishingConfig.downloadCommit.get()
            }
            author = login.username
            token = login.token!!
            description = publishingConfig.description
            browseUrl = publishingConfig.vcsUrl.get()
            issuesUrl = publishingConfig.issuesUrl.get()
            iconUrl = publishingConfig.issuesUrl.get()
            this.downloadProvider

        }
        assetsApi.assetPost(assetDetails)
    }
}