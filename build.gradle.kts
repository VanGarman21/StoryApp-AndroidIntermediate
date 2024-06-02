import com.github.javaparser.utils.Utils.set

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

ext {
    set("lifeCycleVersion", "2.4.1")
    set("circleImageViewVersion", "3.1.0")
    set("retrofitVersion", "2.9.0")
    set("interceptorVersion", "5.0.0-alpha.2")
    set("coroutineVersion", "1.6.0")
    set("krateVersion", "2.0.0")
    set("glideVersion", "4.13.0")
    set("gsonVersion", "2.8.9")
    set("swipeVersion", "1.1.0")
    set("hilt_version", "2.38.1")
    set("activity_ktx_version", "1.4.0")
    set("timberVersion", "5.0.1")
    set("location_version", "18.0.2")
    set("service_location_version", "20.0.0")
    set("room_version", "2.4.2")
    set("room_paging_version", "2.5.0-alpha02")
    set("IdLingResource_version", "3.4.0")
}
