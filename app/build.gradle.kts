plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
    id("kotlin-kapt")
}

android {
    namespace = "com.itssagnikmukherjee.swipeassignment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.itssagnikmukherjee.swipeassignment"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    //room
    implementation("androidx.room:room-runtime:2.8.2")
    implementation("androidx.room:room-ktx:2.8.2")
    kapt("androidx.room:room-compiler:2.8.2")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    //material icons extended
    implementation(libs.androidx.material.icons.extended.android)

    //koin
    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    implementation("io.insert-koin:koin-core:3.5.6")

    //okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor.v4120)

    //coil
    implementation(libs.coil.compose)

    //retrofit
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)

    //compose navigation
    implementation(libs.androidx.navigation.compose)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}