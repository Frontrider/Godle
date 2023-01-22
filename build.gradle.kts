import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.addExtendsFromRelation

plugins {
    kotlin("jvm") version "1.6.21"
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "io.github.frontrider.godle"
version = "0.19.0"

repositories {
    mavenCentral()
}

allprojects {
    tasks.withType(Javadoc::class.java).all { enabled = false }
}

val openapiDir = "$buildDir/generated"
val generatedLicenseSource = project.buildDir.absolutePath+"/license/src"

java {
    sourceSets {
        main {
            this.java.apply{
                srcDir("$openapiDir/src/main/java")
            }
        }
    }
}

kotlin {
    sourceSets {
        main {
            this.kotlin.apply{
                srcDir(generatedLicenseSource)
            }
        }
    }
}

val functionalTest: SourceSet by sourceSets.creating
val integrationTest: SourceSet by sourceSets.creating

addExtendsFromRelation("integrationTestImplementation", "testImplementation")
addExtendsFromRelation("functionalTestImplementation", "testImplementation")

dependencies {

    implementation(gradleApi())
    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")
    //downloader plugin.
    implementation("fi.linuxbox.gradle:gradle-download:0.6")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.platform:junit-platform-runner:1.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")

}

pluginBundle {
    vcsUrl = "https://github.com/Frontrider/Godle"
    website = vcsUrl

    tags = listOf("game development", "godot")
}

gradlePlugin {
    isAutomatedPublishing = false

    plugins {
        create("godle") {
            id = group.toString()
            displayName = "Godle"
            implementationClass = "io.github.frontrider.godle.Godle"
            description = "Plugin to manage small tasks around godot, like addons and the godot binary itself."
        }
    }
    testSourceSets(functionalTest)
    testSourceSets(integrationTest)
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            groupId = project.group.toString()
            artifactId ="godle"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

val functionalTestTestTask = tasks.register<Test>("functionalTest") {
    description = "Runs the functional tests."
    group = "verification"
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath
    mustRunAfter(tasks.test)
}

val integrationTestTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    mustRunAfter(tasks.test)
}

tasks.check {
    dependsOn(functionalTestTestTask)
    dependsOn(integrationTestTestTask)
}

tasks.withType<Test> {
    dependsOn(tasks.pluginUnderTestMetadata)
    useJUnitPlatform {
        maxParallelForks = 8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
