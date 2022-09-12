@file:Suppress("UnstableApiUsage")

import com.petsapp.petfinder.util.libs

plugins {
    `kmm-shared-module-plugin`
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

dependencies {

    commonMainImplementation(projects.shared.domainCore.entity)
    commonMainImplementation(projects.shared.domainCore.util)

    commonMainImplementation(projects.shared.domain.home.dataSource)

    commonMainImplementation(projects.shared.data.infrastructure.network)
    commonMainImplementation(projects.shared.data.infrastructure.cache)

    commonMainImplementation(libs.koin.core)

}
