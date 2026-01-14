plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

        kotlin("plugin.serialization") version "2.0.21"

}

android {
    namespace = "com.example.mymedifetch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mymedifetch"
        minSdk = 29
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
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3)

    // Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Material icons
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation(libs.play.services.auth)
    implementation(libs.androidx.foundation)
    implementation(libs.ui)
    implementation(libs.androidx.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Supabase & Coroutines ---

//    implementation("io.github.jan-tennert.supabase:supabase-kt:2.5.4")
//    implementation("io.github.jan-tennert.supabase:auth-kt:2.5.4")
//    implementation("io.Ktor:Ktor-client-okhttp:2.3.7")


//    implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))
//    implementation("io.github.jan-tennert.supabase:postgrest-kt")
//    implementation("io.ktor:ktor-client-android:3.3.3")
//    implementation("io.github.jan-tennert.supabase:auth:2.5.0")

//    implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))
//
//    implementation("io.github.jan-tennert.supabase:auth")
//    implementation("io.github.jan-tennert.supabase:postgrest")
//    implementation("io.ktor:ktor-client-android:3.3.3")

//    dependencies {
//        implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))
//
//        implementation("io.github.jan-tennert.supabase:supabase") // ✅ REQUIRED
//        implementation("io.github.jan-tennert.supabase:auth")
//        implementation("io.github.jan-tennert.supabase:postgrest")
//
//        implementation("io.ktor:ktor-client-android:3.3.3")
//    }


//    dependencies {
//
//        // Supabase BOM
//        implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))
//
//        // Supabase modules
//        implementation("io.github.jan-tennert.supabase:auth")
//        implementation("io.github.jan-tennert.supabase:postgrest")
//
//        // Ktor (REQUIRED)
//        implementation("io.github.jan-tennert.supabase:ktor")
//
//        // Android Ktor engine
//        implementation("io.ktor:ktor-client-android:3.3.3")
//    }

//    dependencies {
//
//        // Supabase BOM
//        implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))
//
//        // ✅ CORE CLIENT (THIS WAS MISSING)
//        implementation("io.github.jan-tennert.supabase:supabase-kt")
//
//        // Supabase modules
//        implementation("io.github.jan-tennert.supabase:auth")
//        implementation("io.github.jan-tennert.supabase:postgrest")
//
//        // Supabase networking
//        implementation("io.github.jan-tennert.supabase:ktor")
//
//        // Android HTTP engine
//        implementation("io.ktor:ktor-client-android:3.3.3")
//    }

    dependencies {
        // Supabase BOM ensures all Supabase modules use compatible versions
        implementation(platform("io.github.jan-tennert.supabase:bom:3.3.0"))

        // Core Supabase client
        implementation("io.github.jan-tennert.supabase:supabase-kt")

        // Supabase modules
        implementation("io.github.jan-tennert.supabase:auth")
        implementation("io.github.jan-tennert.supabase:postgrest")

        // Optional: Supabase networking with Ktor
        implementation("io.github.jan-tennert.supabase:ktor")

        // Android HTTP engine for Ktor
        implementation("io.ktor:ktor-client-android:3.3.3")
    }





    //implementation("io.github.jan-tennert.supabase:auth-kt")
   // implementation("io.github.jan-tennert.supabase:realtime-kt")



    //implementation("io.supabase:supabase-kt:0.11.0")  // Supabase Kotlin client
   // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
   // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// async

   // implementation("io.github.supabase-community:supabase-kt:0.6.0") // or latest version

    // Coroutines (required for async calls)



}
