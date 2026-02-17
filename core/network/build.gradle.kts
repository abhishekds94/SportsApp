plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinSerialization)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.sportsapp.core.network"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${project.findProperty("SPORTS_DB_API_KEY") ?: "3"}\"")
    }

    buildFeatures {
        buildConfig = true
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
    implementation(project(":core:common"))

    implementation(Dependencies.coreKtx)

    // Networking
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitKotlinxSerialization)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttpLoggingInterceptor)
    implementation(Dependencies.kotlinxSerializationJson)

    // Hilt - Using KSP
    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.mockk)
}