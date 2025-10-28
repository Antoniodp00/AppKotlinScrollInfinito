plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.proyecto.stadiumapp"
    compileSdk = 36
    // O la versión más reciente que tengas

    defaultConfig {
        applicationId = "com.proyecto.stadiumapp"
        minSdk = 26
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

    // Habilitamos ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Material Design 3
    implementation("com.google.android.material:material:1.11.0")

    // Coroutines (para API en segundo plano)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Retrofit (para peticiones API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson (para convertir JSON a Kotlin)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil (para cargar imágenes desde URL)
    implementation("io.coil-kt:coil:2.6.0")

    // OSMdroid (para el mapa gratuito)
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation(libs.androidx.activity)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Dependencia para la Splash Screen API de Android 12+
    implementation("androidx.core:core-splashscreen:1.0.1")
}