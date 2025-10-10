plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("kapt")
}

android {
    namespace = "com.example.application"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.application"
        minSdk = 24
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
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Import the Firebase BoM (Bill of Materials)
    // This manages the versions of all Firebase libraries for you.
    implementation(platform(libs.firebase.bom))

    // Add the Firebase dependencies you need WITHOUT specifying versions.
    // The BoM handles this. Note the use of .ktx libraries for Kotlin extensions.
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database.ktx)

    // Test implementations
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
