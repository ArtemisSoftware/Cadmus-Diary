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
include(":navigation")
include(":util")

include(":core:data")
include(":core:domain")
include(":core:ui")

include(":feature:home:domain")
include(":feature:home:presentation")

include(":feature:write:domain")
include(":feature:write:presentation")

include(":feature:authentication:data")
include(":feature:authentication:domain")







include(":feature:authentication:presentation")
