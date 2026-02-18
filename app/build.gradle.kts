plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

android {
    namespace = "com.sportsapp"
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "com.sportsapp"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:network"))

    implementation(project(":data:teams"))
    implementation(project(":data:leagues"))

    implementation(project(":domain:teams"))
    implementation(project(":domain:leagues"))

    implementation(project(":feature:leagues"))
    implementation(project(":feature:search"))
    implementation(project(":feature:teamdetail"))

    // Android Core
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.activityCompose)

    // Compose
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.composeUI)
    implementation(Dependencies.composeUIGraphics)
    implementation(Dependencies.composeUIToolingPreview)
    implementation(Dependencies.composeMaterial3)
    implementation(Dependencies.composeMaterialIconsExtended)


    // Navigation
    implementation(Dependencies.navigationCompose)

    // Hilt
    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)
    implementation(Dependencies.hiltNavigationCompose)

    // Testing
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espresso)
    androidTestImplementation(platform(Dependencies.composeBom))
    androidTestImplementation(Dependencies.composeUITestJunit4)
    debugImplementation(Dependencies.composeUITooling)
    debugImplementation(Dependencies.composeUITestManifest)
}