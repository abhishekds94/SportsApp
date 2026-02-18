plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

android {
    namespace = "com.sportsapp.domain.teams"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
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
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.javaxInject)
}
