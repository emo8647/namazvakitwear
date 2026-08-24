plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.emo8647.namazvakitwear"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.emo8647.namazvakitwear"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Wear OS Compose Material3 (Slider, InlineSlider ve Material You bileşenleri)
    implementation("androidx.wear.compose:compose-material3:1.0.0-alpha18")
    implementation("androidx.wear.compose:compose-foundation:1.3.0")
    implementation("androidx.wear.compose:compose-navigation:1.3.0")

    // Wear OS Tile & Protolayout (Widget desteği)
    implementation("androidx.wear.tiles:tiles:1.3.0")
    implementation("androidx.wear.protolayout:protolayout:1.1.0")

    // API & Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // GPS & Konum
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Offline Veritabanı (Room DB)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
