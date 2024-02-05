# Library Exporter

## Overview

🚀 Gradle-based utility simplifies the process of exporting project dependencies and repositories in data format.

## Features

- **Dependency Export**: Automatically extracts project dependencies from Gradle and exports them to a YAML file.
- **Module Support**: Bundles and exports dependencies from project modules.

- **Version Tracking**: Ensures accurate version information for each dependency in the exported YAML or JSON file.
- **Simplified Documentation**: Provides a clear and concise data format for easy integration with documentation and
  version control.

## Getting Started

```groovy
plugins {
    id("java")
    id("com.fitchle.gradle.libraryexporter") version "1.0"
}

libraryExporter {
    // excludeDependency(String), excludeRepository(URI), exportType(ExportType), parentModule(Project), addModules(Project...)
}
```

Alternatively, the plugin can be added to the buildscript classpath and applied:

````groovy
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath 'com.fitchle.gradle.libraryexporter:1.0'
    }
}
apply plugin: 'java'
apply plugin: 'com.fitchle.gradle.libraryexporter'
````

## Example Outputs (JSON & Yaml)

````yaml
repositories:
  '0':
    url: https://repo.maven.apache.org/maven2/
  '1':
    url: https://jitpack.io
dependencies:
  '0':
    group: com.google.guava
    name: listenablefuture
    version: 9999.0-empty-to-avoid-conflict-with-guava
  '1':
    group: com.google.errorprone
    name: error_prone_annotations
    version: 2.7.1
  '2':
    group: commons-lang
    name: commons-lang
    version: '2.6'
  '3':
    group: com.google.guava
    name: failureaccess
    version: 1.0.1
````

````json
{
  "repositories": [
    {
      "url": "https://repo.maven.apache.org/maven2/"
    },
    {
      "url": "https://jitpack.io"
    }
  ],
  "dependencies": [
    {
      "group": "com.google.guava",
      "name": "listenablefuture",
      "version": "9999.0-empty-to-avoid-conflict-with-guava"
    },
    {
      "group": "com.google.errorprone",
      "name": "error_prone_annotations",
      "version": "2.7.1"
    },
    {
      "group": "commons-lang",
      "name": "commons-lang",
      "version": "2.6"
    },
    {
      "group": "com.google.guava",
      "name": "failureaccess",
      "version": "1.0.1"
    }
  ]
}
````

## How to Build

1. 📥 Clone this repository to your local machine.
2. 🛠️ Build project with gradle.
3. 🔌 Integrate the Gradle Library Exporter into your Gradle project.

