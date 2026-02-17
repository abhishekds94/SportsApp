plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.sportsapp.data.events"
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
    implementation(project(":data:teams"))
    implementation(project(":domain:teams"))

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coreKtx)

    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.coroutinesTest)
}