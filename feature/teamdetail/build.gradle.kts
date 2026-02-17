plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.sportsapp.feature.teamdetail"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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
    implementation(project(":core:designsystem"))
    implementation(project(":data:teams"))
    implementation(project(":data:events"))

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.lifecycleViewModelCompose)
    implementation(Dependencies.lifecycleRuntimeCompose)

    // Compose
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.composeUI)
    implementation(Dependencies.composeUIGraphics)
    implementation(Dependencies.composeUIToolingPreview)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeMaterialIconsExtended)
    debugImplementation(Dependencies.composeUITooling)

    // Hilt
    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)
    implementation(Dependencies.hiltNavigationCompose)

    testImplementation(Dependencies.junit)
}