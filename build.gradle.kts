import java.net.URI
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("maven-publish")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "de.varilx"
version = project.property("project_version") as String

repositories {
    mavenCentral()
//  mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://reposilite.varilx.de/Varilx")
}

dependencies {
    // PaperMC API
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    compileOnly("org.projectlombok:lombok:1.18.36")

    // JetBrains Annotations
    implementation("org.jetbrains:annotations:26.0.1")

    // Base API
    implementation("de.varilx:base-api:0.2.0")

    // JDA
    implementation("net.dv8tion:JDA:5.2.0")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}


tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
}

publishing {
    repositories {
        maven {
            name = "Reposilite"
            url = URI("https://reposilite.varilx.de/Varilx")
            credentials {
                username = "Dario"
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "$group"
            version = version
            artifact(tasks.named<ShadowJar>("shadowJar").get())
            artifact(tasks.named<Jar>("sourcesJar").get())
        }
    }
}


tasks.named("publishGprPublicationToReposiliteRepository") {
    dependsOn(tasks.named("jar"))
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }
}