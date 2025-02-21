plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin") // for Safe Args
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.foodplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.foodplanner"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.11.0") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // for JSON parsing
    implementation("com.google.code.gson:gson:2.11.0") // Gson
    implementation("com.github.bumptech.glide:glide:4.16.0") // for image loading
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1") // Coroutines
    implementation("androidx.room:room-ktx:2.7.0-alpha12")
    implementation("androidx.room:room-runtime:2.7.0-alpha12")
    kapt("androidx.room:room-compiler:2.7.0-alpha12")
    implementation("androidx.navigation:navigation-fragment:2.5.3") // for Navigation component with fragments
    implementation("androidx.navigation:navigation-ui:2.5.3") // for Navigation component with UI
    // implementation("com.google.android.material:material:1.6.1")// Material Design
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.afollestad.material-dialogs:bottomsheets:3.3.0")
    implementation("com.airbnb.android:lottie:5.0.3")
    implementation("com.github.GrenderG:Toasty:1.5.2")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")


}