plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.lorenzovainigli.onboarding"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.runtime.livedata)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
}