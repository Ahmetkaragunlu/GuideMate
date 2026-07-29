plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlin.ktfmt)
  alias(libs.plugins.secrets.gradle.plugin)
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
    }
    debug {}
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

  // Local preferences
  implementation(libs.androidx.datastore.preferences)

  // DI
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
  implementation(libs.androidx.hilt.navigation.compose)

  // Navigation
  implementation(libs.androidx.navigation.compose)
  implementation(libs.kotlinx.serialization.core)

  // Icons
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.tabler.icons)

  // NETWORK (Retrofit & Gson)
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.gson)

  // OKHTTP
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)

  // Google Authentication
  implementation(libs.androidx.credentials)
  implementation(libs.androidx.credentials.play.services.auth)
  implementation(libs.google.id)

  // Google Places
  implementation(libs.places)
}

secrets { defaultPropertiesFileName = "local.defaults.properties" }

ktfmt { googleStyle() }
