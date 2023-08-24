buildscript {
    repositories {
        google()
        mavenCentral()
    }
    val firebaseEnabled = true
    dependencies {
        if (firebaseEnabled) {
            classpath("com.google.gms:google-services:4.3.15")
            classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.8")
        }
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
}
true // Needed to make the Suppress annotation work for the plugins block