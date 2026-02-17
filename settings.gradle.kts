pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SportsApp"

// Core modules
include(":core:common")
include(":core:network")
include(":core:designsystem")

// Data modules
include(":data:teams")
include(":data:events")

// Feature modules
include(":feature:leagues")
include(":feature:search")
include(":feature:teamdetail")

// App module
include(":app")