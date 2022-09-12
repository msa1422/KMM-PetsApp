@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.petsapp.petfinder.util.libs
import com.petsapp.petfinder.extensions.BuildType
import com.petsapp.petfinder.extensions.*
import com.petsapp.petfinder.util.PackageNameAccessor

plugins {
    id("com.android.application")
    kotlin("android")
}

android {

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = PackageNameAccessor.APP_PACKAGE
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        multiDexEnabled = true
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = "app-${variant.baseName}-${variant.buildType.name}-${variant.versionName}.apk"
            }
    }

    signingConfigs {
        create(BuildType.RELEASE) {
            storeFile = file(getKeystoreProperty("STORE_FILE"))
            storePassword = getKeystoreProperty("STORE_PASSWORD")
            keyAlias = getKeystoreProperty("KEY_ALIAS")
            keyPassword = getKeystoreProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
            signingConfig = signingConfigs.getByName(BuildType.RELEASE)
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            isTestCoverageEnabled = BuildTypeRelease.isTestCoverageEnabled
        }
        getByName(BuildType.DEBUG) {
            applicationIdSuffix = BuildTypeDebug.applicationIdSuffix
            versionNameSuffix = BuildTypeDebug.versionNameSuffix
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            isTestCoverageEnabled = BuildTypeDebug.isTestCoverageEnabled
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    dependencies {
        debugImplementation(libs.squareup.leakcanary)
    }
}
