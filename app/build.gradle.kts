plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.bpregister"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bpregister"
        minSdk = 30
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
    dataBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val kspversion = "2.6.1"
    val lifecycleversion = "2.8.7"
    //--room Database dependencies
    annotationProcessor( "androidx.room:room-compiler:$kspversion")
    implementation("androidx.room:room-runtime:$kspversion")
    implementation( "androidx.room:room-paging:$kspversion")
    implementation("androidx.room:room-ktx:$kspversion")
    //KSP
    ksp("androidx.room:room-compiler:$kspversion")
    //--room end
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleversion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleversion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
//    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")

    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
//    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
}