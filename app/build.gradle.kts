//import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.bpregister"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bpregister"
        minSdk = 29
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
//    fun Packaging.() {
//        resources.excludes.add("META-INF/atomicfu.kotlin_module")
//    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //--room Database dependencies
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor( "androidx.room:room-compiler:2.5.2")
    // To use Kotlin annotation processing tool (kapt)
    annotationProcessor( "androidx.room:room-compiler:2.5.2")
    // To use Kotlin Symbol Processing (KSP)
    annotationProcessor( "androidx.room:room-compiler:2.5.2")
//    // optional - RxJava2 support for Room
//    implementation( "androidx.room:room-rxjava2:2.5.2")
//    // optional - RxJava3 support for Room
//    implementation( "androidx.room:room-rxjava3:2.5.2")
//    // optional - Guava support for Room, including Optional and ListenableFuture
//    implementation( "androidx.room:room-guava:2.5.2")
    // optional - Paging 3 Integration
    implementation( "androidx.room:room-paging:2.5.2")
    //--room Database dependencies end
    
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // optional - Room Test helpers
    testImplementation("androidx.room:room-testing:2.5.2")

}