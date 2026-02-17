plugins {
    id(Plugins.androidApplication) version Versions.androidGradlePlugin apply false
    id(Plugins.androidLibrary) version Versions.androidGradlePlugin apply false
    id(Plugins.kotlinAndroid) version Versions.kotlin apply false
    id(Plugins.kotlinSerialization) version Versions.kotlin apply false
    id(Plugins.hilt) version Versions.hilt apply false
    id(Plugins.ksp) version Versions.ksp apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}