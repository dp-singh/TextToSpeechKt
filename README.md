<div align="center">

  # TextToSpeechKt
  Multiplatform Text-to-Speech library for Android and Browser (JS).

This library will enable you to use Text-to-Speech in multiplatform Kotlin projects and is useful when working with [Jetpack Compose on Android](https://developer.android.com/jetpack/compose) & [web (currently in preview)](https://compose-web.ui.pages.jetbrains.team/).

  [![Gradle deployment](https://github.com/Marc-JB/TextToSpeechKt/actions/workflows/deployment.yml/badge.svg)](https://github.com/Marc-JB/TextToSpeechKt/actions) 
  [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Marc-JB_TextToSpeechKt&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Marc-JB_TextToSpeechKt) 
  [![Maven Central](https://badgen.net/maven/v/maven-central/nl.marc-apps/tts)](https://search.maven.org/search?q=g:%22nl.marc-apps%22%20AND%20a:%22tts%22)
  [![License](https://badgen.net/github/license/Marc-JB/TextToSpeechKt)](https://github.com/Marc-JB/TextToSpeechKt/blob/main/LICENSE)

</div>

# :notebook_with_decorative_cover: Table of Contents
- [About the Project](#star2-about-the-project)
  * [Tech Stack](#space_invader-tech-stack)
  * [Features](#dart-features)
- [Getting Started](#toolbox-getting-started)
  * [Prerequisites](#bangbang-prerequisites)
  * [Installation](#gear-installation)
- [Usage](#eyes-usage)
- [License](#warning-license)
- [Acknowledgements](#gem-acknowledgements)

## :star2: About the Project
### :space_invader: Tech Stack
Uses Kotlin Multiplatform with an Android and JavaScript/Browser target. 

### :dart: Features
- Create the engine with Kotlin Coroutines
- Listen for speech synthesis completion using Kotlin Coroutines
- Modify the volume or mute the volume entirely
- Modify the voice pitch
- Modify the voice rate

## 	:toolbox: Getting Started
### :bangbang: Prerequisites
A build tool like Gradle or Maven. NPM is not supported yet.

### :gear: Installation
<details>
  <summary><strong>Gradle</strong></summary>

  Configure the Maven Central repository:  
  ```Kotlin
  repositories {
      mavenCentral()
  }
  ```

  And add the library to your dependencies:  
  ```Kotlin
  dependencies {
      implementation("nl.marc-apps:tts:1.0.0")
  }
  ```  
  Make sure to configure the latest stable version: [![Maven Central](https://badgen.net/maven/v/maven-central/nl.marc-apps/tts)](https://search.maven.org/search?q=g:%22nl.marc-apps%22%20AND%20a:%22tts%22)   

</details>

<details>
  <summary><strong>Apache Maven</strong></summary>

  Add the library to your dependencies:  
  ```XML
  <dependency>
      <groupId>nl.marc-apps</groupId>
      <artifactId>tts</artifactId>
      <version>1.0.0</version>
  </dependency>
  ```  
  Make sure to configure the latest stable version: [![Maven Central](https://badgen.net/maven/v/maven-central/nl.marc-apps/tts)](https://search.maven.org/search?q=g:%22nl.marc-apps%22%20AND%20a:%22tts%22)   

</details>

## :eyes: Usage
Note: the examples are not using Jetpack Compose yet. 

### Generated docs by Dokka
Documentation for:
- [The current version](https://marc-jb.github.io/TextToSpeechKt/tts/nl.marc_apps.tts/index.html)
- [The previous version](https://marc-jb.github.io/TextToSpeechKt/previous/tts/nl.marc_apps.tts/index.html)

### Android example
See the [/app](/app) directory for a working example that you can try out on Android. 
This example is written using Kotlin/JVM.

### Browser (Kotlin/JS) example
See the [/browser](/browser) directory for a working example that you can try out in the browser. 
This example is written using Kotlin/JS.

### Browser (plain JavaScript) example
See the [/browser_js](/browser_js) directory for a working example that you can try out in the browser.
This example is written using plain JavaScript.

### Code snippet
```Kotlin
import nl.marc_apps.tts.TextToSpeech
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechInitialisationError

var tts: TextToSpeechInstance? = null

@Throws(TextToSpeechInitialisationError::class)
suspend fun sayHello(name: String = "world") {
    // Use TextToSpeech.createOrNull to ignore errors.
    tts = tts ?: TextToSpeech.createOrThrow(applicationContext)
    
    // Use status STARTED to resume coroutine when the TTS engine starts speaking. The status of FINISHED will wait until the TTS engine has finished speaking.
    tts?.say("Hello $name!", clearQueue = false, resumeOnStatus = TextToSpeechInstance.Status.FINISHED)
}

fun onApplicationExit() {
    tts?.close()
}
```

## :warning: License
This project is published under the MIT License. Read more about this license in the `LICENSE` file. 

## :gem: Acknowledgements
 - [Awesome Readme Template](https://github.com/Louis3797/awesome-readme-template)
 - [Badgen](https://badgen.net/)
