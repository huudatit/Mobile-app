plugins {
    alias(libs.plugins.android.application) apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3") // Phiên bản Gradle Plugin
        classpath("com.google.gms:google-services:4.4.0") // Plugin Google Services
    }
}
