import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.app.cash.paparazzi)
    alias(libs.plugins.com.google.devtools.ksp)
}

var buildFoss = false

android {
    namespace = "com.lorenzovainigli.foodexpirationdates"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lorenzovainigli.foodexpirationdates"
        minSdk = 24
        targetSdk = 34
        versionCode = 22
        versionName = "2.0"

        archivesName.set("FoodExpirationDates-$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    configurations {
        all {
            exclude(module = "kotlin-stdlib-jdk7")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("full") {
            dimension = "version"
        }
        if (buildFoss) {
            create("foss") {
                dimension = "version"
                applicationIdSuffix = ".foss"
                versionNameSuffix = "-foss"
            }
        }
    }

    sourceSets {
        getByName("debug").assets.srcDirs(files("$projectDir/schemas")) // Room
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = false
    }
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

dependencies {

    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.uiautomator)

    // Jetpack Compose
    // https://developer.android.com/jetpack/compose/bom
    // https://developer.android.com/jetpack/compose/bom/bom-mapping
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.runtime.livedata)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // Dagger
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.work.runtime.ktx)

    // Firebase
    // https://firebase.google.com/docs/android/setup#available-libraries
    "fullImplementation"(platform(libs.firebase.bom))
    "fullImplementation"(libs.firebase.analytics)
    "fullImplementation"(libs.firebase.crashlytics)

    // Splash Screen
    implementation(libs.splashscreen)

}

if (!buildFoss){
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

apply(plugin = "com.google.dagger.hilt.android")


tasks.register<Copy>("copyAPKs") {
    from(layout.buildDirectory.dir("outputs/apk")) {
        include("**/*.apk")
        exclude("androidTest/**")
        eachFile {
            path = name.replace("-full", "")
                .replace("-release", "")
        }
        includeEmptyDirs = false
    }
    from(layout.buildDirectory.dir("outputs/bundle")) {
        include("**/*-release.aab")
        eachFile {
            path = name.replace("-full", "")
                .replace("-release", "")
        }
        includeEmptyDirs = false
    }
    into(layout.projectDirectory.dir("apk"))
}

tasks.register("generateAPKs") {
    dependsOn("assembleFullDebug", "copyAPKs")
}