import com.petsapp.petfinder.util.libs
import com.petsapp.petfinder.util.PackageNameAccessor.CORE_RESOURCES_PACKAGE

plugins {
    `kmm-shared-module-plugin`
    id("dev.icerock.mobile.multiplatform-resources")
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

dependencies {
    commonMainApi(libs.moko.resources)
    androidMainApi(libs.moko.resources.compose)
}

multiplatformResources {
    multiplatformResourcesPackage = CORE_RESOURCES_PACKAGE
    multiplatformResourcesClassName = "CoreR" // optional, default MR
}
