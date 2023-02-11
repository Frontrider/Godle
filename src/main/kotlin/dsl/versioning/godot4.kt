@file:Suppress("unused")

package io.github.frontrider.godle.dsl.versioning

import io.github.frontrider.godle.dsl.GodleExtension


infix fun GodleExtension.asGodot4Beta(version: String){
    this.version.set(godotBeta("4.0",version))
}



infix fun GodleExtension.asGodot4releaseCandidate(version: String){
    this.version.set(godotReleaseCandidate("4.0",version))
}
