import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.serialization)
}



android {
    namespace = "com.example.bikejoyapp"


    defaultConfig {
        applicationId = "com.example.bikejoyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            resValue(
                "string",
                "google_maps_key",
                gradleLocalProperties(rootDir).getProperty("apiKey")
            )
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileSdk = 34
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.foundation.layout.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Map
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    //Location
    implementation(libs.play.services.location)


    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose.v140alpha02)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.kotlinx.coroutines.core)


    implementation(libs.androidx.runtime.livedata.v161)

    implementation(libs.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.fragment.ktx)

    // Google Places
    implementation (libs.places)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.kotlinx.coroutines.play.services)

    implementation (libs.android.maps.utils)
    implementation(libs.mockito.core)
    implementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation (libs.io.mockk.mockk5)
    testImplementation (libs.androidx.core.testing.v210)
    androidTestImplementation (libs.androidx.junit.v112)
    androidTestImplementation (libs.androidx.espresso.core.v330)
    androidTestImplementation (libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    implementation (libs.androidx.core.ktx.v170)
    androidTestImplementation (libs.androidx.espresso.core.v340)
    androidTestImplementation (libs.androidx.runner)
    androidTestImplementation (libs.androidx.rules)

    implementation(libs.kotlinx.serialization.json.v132)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.compose.bom.v20240401)
}

fun gradleLocalProperties(rootDir: File): Properties {
    val localProperties = Properties()
    localProperties.load(File(rootDir, "local.properties").inputStream())
    return localProperties
}
