plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

android {
    namespace = "com.sportsapp.core.common"
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
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coroutinesCore)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.coroutinesTest)
}