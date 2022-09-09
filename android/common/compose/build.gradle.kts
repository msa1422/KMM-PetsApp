@file:Suppress("UnstableApiUsage")

import com.petsapp.petfinder.constants.AndroidModule
import com.petsapp.petfinder.constants.SharedModule
import com.petsapp.petfinder.util.libs

plugins {
    `android-common-plugin`
    kotlin("plugin.serialization")
}

android {
    namespace = AndroidModule.Common.Compose.PACKAGE
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.serialization)

    implementation(libs.bundles.app.ui)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.navigation.animation)

    implementation(project(SharedModule.DomainCore.Util.MODULE))
    implementation(project(SharedModule.DomainCore.Resources.MODULE))

    implementation(project(AndroidModule.Common.Resources.MODULE))

}