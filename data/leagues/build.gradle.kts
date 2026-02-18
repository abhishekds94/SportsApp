plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.ksp)
    id(Plugins.kotlinSerialization)
}

android {
    namespace = "com.sportsapp.data.leagues"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":domain:leagues"))

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coreKtx)

    // Networking
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitKotlinxSerialization)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.okhttpLoggingInterceptor)
    implementation(Dependencies.kotlinxSerializationJson)

    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)

    // Testing
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.coroutinesTest)
    testImplementation(Dependencies.turbine)
    testImplementation(Dependencies.kotlinTest)
}
