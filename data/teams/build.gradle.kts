plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.ksp)
    id(Plugins.kotlinSerialization)
}

android {
    namespace = "com.sportsapp.data.teams"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":core:network"))
    implementation(project(":domain:teams"))

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.kotlinxSerializationJson)
    implementation(Dependencies.retrofitKotlinxSerialization)

    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)

    // Room (offline cache)
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKtx)
    ksp(Dependencies.roomCompiler)

    // Testing
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.coroutinesTest)
    testImplementation(Dependencies.turbine)
    testImplementation(Dependencies.kotlinTest)
}