@file:Suppress("UnstableApiUsage")

import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
    id("org.jetbrains.compose")
    alias(libs.plugins.dokka)
}

val useWasmTarget = "wasm" in libs.versions.tts.get()
val projectId = "compose"
val jvmVersion = JavaVersion.VERSION_1_8

group = getTtsProperty("groupId")!!
version = libs.versions.tts.get()

kotlin {
    js("browserJs", IR) {
        browser()
        binaries.executable()
    }

    if (useWasmTarget) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs("browserWasm") {
            browser()
            binaries.executable()
        }
    }

    androidTarget {
        publishLibraryVariantsGroupedByFlavor = true
        publishAllLibraryVariants()
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = jvmVersion.toString()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                if (useWasmTarget) {
                    implementation(libs.kotlin.coroutines.wasm)
                } else {
                    implementation(libs.kotlin.coroutines)
                }
                api(project(":tts"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.foundation)
            }
        }
        val browserJsMain by getting {}
        if (useWasmTarget) {
            val browserWasmMain by getting {}
            val browserMain by creating {
                dependsOn(commonMain)
                browserJsMain.dependsOn(this)
                browserWasmMain.dependsOn(this)
            }
        } else {
            val browserMain by creating {
                dependsOn(commonMain)
                browserJsMain.dependsOn(this)
            }
        }
    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    namespace = getTtsScopedProperty("namespace")

    defaultConfig {
        minSdk = 21

        setProperty("archivesBaseName", getTtsScopedProperty("artifactId"))
    }

    compileOptions {
        sourceCompatibility = jvmVersion
        targetCompatibility = jvmVersion
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.extensions.get()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = jvmVersion.toString()
    }
}

dependencies {
    dokkaPlugin(libs.dokka.plugins.androidDocs)
    dokkaPlugin(libs.dokka.plugins.versioning)
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/${name}/kotlin"))
            remoteUrl.set(URL("https://${getTtsProperty("git", "location")}/blob/main/${getTtsScopedProperty("artifactId")}/src/${name}/kotlin"))
            remoteLineSuffix.set("#L")
        }

        externalDocumentationLink {
            url.set(URL(getTtsProperty("documentation", "url")))
            packageListUrl.set(URL("${getTtsProperty("documentation", "url")}/package-list"))
        }

        jdkVersion.set(jvmVersion.majorVersion.toInt())
    }
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaHtmlPartial)
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.asFile.get().resolve("dokka"))
}

publishing {
    repositories {
        configureOssrhRepository("SNAPSHOT" in libs.versions.tts.get(), getConfigProperty("ossrh", "username"), getConfigProperty("ossrh", "password"))

        configureGitHubPackagesRepository("Marc-JB", "TextToSpeechKt", getConfigProperty("gpr", "user"), getConfigProperty("gpr", "key"))
    }

    publications {
        withType<MavenPublication> {
            configureMavenPublication(project, this, javadocJar, getTtsScopedProperty("artifactId")!!)
        }
    }
}

signing {
    isRequired = true

    val signingKey = getConfigProperty("gpg", "signing", "key")
    val signingPassword = getConfigProperty("gpg", "signing", "password")
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications)
}

private fun getTtsScopedProperty(vararg path: String) = getTtsProperty(projectId, *path)
