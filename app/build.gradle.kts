plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.dicoding.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.storyapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.activity:activity-ktx:1.8.0")

    //Retrofit
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //ViewModel & LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //Debug Network
    debugImplementation ("com.github.chuckerteam.chucker:library:4.0.0")

    //Preference
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    //Coil
    implementation("io.coil-kt:coil:2.4.0")

    //Paging
    implementation("androidx.room:room-paging:2.6.0")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")

    //Room
    implementation ("androidx.room:room-ktx:2.6.0")
    kapt( "androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-runtime:2.6.0")

    //Location
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    //Testing
    testImplementation ("androidx.arch.core:core-testing:2.2.0") // InstantTaskExecutorRule
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") //TestDispatcher
    testImplementation ("org.mockito:mockito-core:4.4.0")
    testImplementation ("org.mockito:mockito-inline:4.4.0")

    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    implementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")

}