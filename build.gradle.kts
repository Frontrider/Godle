import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.addExtendsFromRelation

plugins {
    kotlin("jvm") version "1.6.21"
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.0.0"
}

group = "io.github.frontrider.godle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val functionalTest: SourceSet by sourceSets.creating
val integrationTest: SourceSet by sourceSets.creating

addExtendsFromRelation("integrationTestImplementation","testImplementation")
addExtendsFromRelation("functionalTestImplementation","testImplementation")

dependencies {
    implementation(gradleApi())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")
    //downloader plugin.
    implementation("fi.linuxbox.gradle:gradle-download:0.6")

    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    //json parsing to interact with the godot asset store.
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.4")

    // https://mvnrepository.com/artifact/com.konghq/unirest-java
    // a simple http client, so we can call different apis more easily.
    //Currently this is used to call godot's http api to
    implementation("com.konghq:unirest-java:3.13.12")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.platform:junit-platform-runner:1.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
}
pluginBundle{
    vcsUrl= "<uri to project source repository>"
    website = vcsUrl
    tags = listOf("game development","godot")
}

gradlePlugin {
    isAutomatedPublishing = false

    plugins {
        create("godle") {
            id = group.toString()
            implementationClass = "io.github.frontrider.godle.Godle"
            description = "Plugin to manage small tasks around plugins, like addons and the godot binary itself."
        }
    }
    testSourceSets(functionalTest)
    testSourceSets(integrationTest)
}

publishing {
    publications {
        create<MavenPublication>("maven"){
            groupId = project.group.toString()
            artifactId = project.name
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
    useJUnitPlatform{
        maxParallelForks = 8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
