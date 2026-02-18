object Versions {
    // SDK Versions
    const val compileSdk = 34
    const val minSdk = 24
    const val targetSdk = 34

    // Build Tools
    const val kotlin = "1.9.20"
    const val androidGradlePlugin = "8.1.4"
    const val ksp = "1.9.20-1.0.14"

    // AndroidX Core
    const val coreKtx = "1.12.0"
    const val lifecycleRuntimeKtx = "2.6.2"
    const val activityCompose = "1.8.1"

    // Compose
    const val composeBom = "2023.10.01"
    const val composeCompiler = "1.5.4"
    const val navigation = "2.7.5"

    // Dependency Injection
    const val hilt = "2.48"
    const val hiltNavigationCompose = "1.1.0"

    // Networking
    const val retrofit = "2.9.0"
    const val okhttp = "4.12.0"
    const val kotlinxSerialization = "1.6.0"
    const val retrofitKotlinxSerialization = "1.0.0"

    // Coroutines
    const val coroutines = "1.7.3"

    // Image Loading
    const val coil = "2.5.0"

    // Database
    const val room = "2.6.1"

    // Testing
    const val junit = "4.13.2"
    const val junitExt = "1.1.5"
    const val espresso = "3.5.1"
    const val mockk = "1.13.8"
    const val turbine = "1.0.0"
    const val truth = "1.1.5"
    const val coroutinesTest = "1.7.3"
    const val kotlinTest = "1.9.20"
}

object Dependencies {
    // AndroidX Core
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
    const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycleRuntimeKtx}"
    const val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeKtx}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"

    // Compose BOM
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"

    // Compose (use with BOM)
    const val composeUI = "androidx.compose.ui:ui"
    const val composeUIGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUIToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeMaterial3 = "androidx.compose.material3:material3"
    const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended"
    const val composeUITooling = "androidx.compose.ui:ui-tooling"
    const val composeUITestManifest = "androidx.compose.ui:ui-test-manifest"
    const val composeUITestJunit4 = "androidx.compose.ui:ui-test-junit4"

    // Navigation
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigation}"

    // Dependency Injection
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"

    // Networking
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitKotlinxSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitKotlinxSerialization}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val kotlinxSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"

    // Coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Image Loading
    const val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    // Database
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    // Testing
    const val junit = "junit:junit:${Versions.junit}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlinTest}"
    const val javaxInject = "javax.inject:javax.inject:1"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val kotlinSerialization = "org.jetbrains.kotlin.plugin.serialization"
    const val hilt = "com.google.dagger.hilt.android"
    const val ksp = "com.google.devtools.ksp"
}