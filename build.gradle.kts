// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        val hiltVersion by extra("2.49")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${extra["hiltVersion"]}")
        val nav_version = "2.5.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}