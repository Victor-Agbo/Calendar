plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.victor.calendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.victor.calendar"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    hilt {
        enableAggregatingTask = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.navigation.runtime.ktx)
    implementation(libs.compose.material)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.material)
    implementation(libs.play.services.auth)
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)


    //Room
    implementation(libs.room.runtime)
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.lifecycle.runtime.compose)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.core)
    // Optional -- Mockito framework
    testImplementation(libs.mockito.core)
    // Optional -- mockito-kotlin
    testImplementation(libs.mockito.kotlin)
    // Optional -- Mockk framework
    testImplementation(libs.mockk)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.navigation.testing)
    androidTestImplementation(libs.espresso.intents)
    val androidXTestVersion = "1.5.0"
    androidTestImplementation("androidx.test:runner:$androidXTestVersion")
    androidTestImplementation("androidx.test:rules:$androidXTestVersion")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")


    val uiAutomatorVersion = "2.3.0"
    androidTestImplementation("androidx.test.uiautomator:uiautomator:$uiAutomatorVersion")


    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    kapt(libs.hilt.android.compiler)

    // Make Hilt generate code in the androidTest folder
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
}