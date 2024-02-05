plugins {
    java
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.fitchle.gradle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api("org.yaml:snakeyaml:2.2")
    api("com.google.code.gson:gson:2.10.1")
}

gradlePlugin {
    val plugin by plugins.creating {
        id = "com.fitchle.gradle.libraryexporter"
        version = "1.0"
        implementationClass = "com.fitchle.gradle.libraryexporter.LibraryExporterPlugin"
    }
}