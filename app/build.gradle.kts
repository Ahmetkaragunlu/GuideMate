plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlin.ktfmt)
}

android {
  namespace = "com.ahmetkaragunlu.guidemate"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.ahmetkaragunlu.guidemate"
    minSdk = 30
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      buildConfigField("String", "BASE_URL", "\"https://api.guidemate.com/api/v1/auth/\"")
    }
    debug { buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/v1/auth/\"") }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  // ViewModel
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // DI
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  implementation(libs.androidx.hilt.navigation.compose)

  // Navigation
  implementation(libs.androidx.navigation.compose)

  // Icons
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.tabler.icons)

  // NETWORK (Retrofit & Gson)
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.gson)

  // OKHTTP
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)

  // EncryptedSharedPreferences
  implementation(libs.androidx.security.crypto)

  // Google Authentication
  implementation(libs.play.services.auth)
}

ktfmt { googleStyle() }
