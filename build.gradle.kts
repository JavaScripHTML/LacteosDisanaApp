// Archivo build.gradle.kts a nivel del proyecto (Top-level)
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("com.android.library") version "8.6.0" apply false
    id("com.google.gms.google-services") version "4.3.14" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

// Define los repositorios globales para todas las dependencias
allprojects {
    repositories {
        google() // Repositorio de Google para dependencias de Android
        mavenCentral() // Repositorio Maven Central
    }
}

// Tarea para limpiar el proyecto
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
