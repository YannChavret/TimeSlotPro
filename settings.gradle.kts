pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io")
    }
}

rootProject.name = "TimeSlotPro"
include(":app")
 