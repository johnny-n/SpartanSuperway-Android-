# Spartan Superway Mobile (Android)
ENGR 195C - Interdisciplinary Senior Capstone Project

# Installing Kotlin
This project uses Kotlin, a JVM language that is 100% interoperable with Java.Kotlin was chosen for this project due to the language's expressiveness, safe type-system, and maintainability over Java.
To install Kotlin, follow these steps:

  1. Install Kotlin plugin
  
    Preferences -> Plugins -> Kotlin
  
  2. Add Kotlin to Gradle
  
  build.Gradle (project)
  ````
  buildscript {
    ...
    ext.kotlin_version = '<latest_kotlin_version>' // Example: ext.kotlin_version = '1.0.6'
    ...
    dependencies {
      ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
  }
  ````
  build.Gradle (app)
  ````
  apply plugin: 'com.android.application'
  apply plugin: 'kotlin-android'
  apply plugin: 'kotlin-android-extensions'

  android {
    ...
    sourceSets {
        main.java.srcDirs += 'src/main/kotlin'
    }
  }

  dependencies {
    ...
    compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
  }
````

# License
````
Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
````
