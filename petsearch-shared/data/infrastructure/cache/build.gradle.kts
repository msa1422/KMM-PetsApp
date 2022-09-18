@file:Suppress("UnstableApiUsage")

import com.msa.petsearch.util.libs

plugins {
    `kmm-shared-module-plugin`
    id("io.realm.kotlin")
}

dependencies {
    commonMainImplementation(projects.petsearchShared.core.util)

    commonMainImplementation(libs.kotlinx.coroutines.core)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.realm.kotlin)
}
