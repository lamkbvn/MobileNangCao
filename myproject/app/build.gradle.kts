plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mobilenangcao"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobilenangcao"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.tensorflow:tensorflow-lite-select-tf-ops:2.11.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}