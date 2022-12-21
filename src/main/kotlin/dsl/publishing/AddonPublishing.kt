package io.github.frontrider.godle.dsl.publishing

import dsl.publishing.AddonType
import dsl.publishing.AssetCategories
import dsl.publishing.CompatVersion
import godle.license.License
import godle.license.MIT
import godot.assets.model.AssetDetails.SupportLevelEnum
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.provider.Providers
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject


/**
 * Allows to publish an addon to the godot asset library.
 * */

fun AddonPublishing.currentCommitHash(): String {
    val repository = FileRepository(project.rootDir)
    val latestCommit: RevCommit = Git(repository).log().setMaxCount(1).call().iterator().next()
    return latestCommit.name
}


class AddonPublishing @Inject constructor(objectFactory: ObjectFactory, val project: Project) {
    //IF set then the plugin will try to update an existing asset(?)
    var id = objectFactory.property(Int::class.java)
    var vcsUrl = objectFactory.property(String::class.java)
    var category = AssetCategories.Tools
    var type = AddonType.ADDON

    //the download commit by default is always set to the last commit.
    var downloadCommit = objectFactory.property(String::class.java).convention(Providers.changing {
        currentCommitHash()
    })
    var description:String =""
    //if download url is set, then we don't use the commit hash.
    var downloadURL = objectFactory.property(String::class.java)
    var license: License = MIT
    var godotVersion = CompatVersion.`3_5`
    var iconUrl = objectFactory.property(String::class.java)

    var issuesUrl = objectFactory.property(String::class.java).convention(Providers.changing {
        "$vcsUrl/issues"
    })
    var supportLevel: SupportLevelEnum = SupportLevelEnum.TESTING
    var title = objectFactory.property(String::class.java).convention(Providers.changing {
        project.name
    })
    var versionString = objectFactory.property(String::class.java).convention(Providers.changing {
        project.version.toString()
    })
    var isArchived = false
    val credentials = Credentials()
    fun credentials(action: Action<in Credentials>) {
        action.execute(credentials)
    }
}