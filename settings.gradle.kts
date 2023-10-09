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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "CadmusDiary"
include(":app")
include(":core:ui")
include(":core:data")
include(":feature:home:presentation")
include(":feature:write:presentation")
include(":feature:authentication:presentation")
include(":core:domain")
include(":feature:authentication:data")
