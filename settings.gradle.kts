pluginManagement {
    repositories {
        google()  // Repositorio de Google para Firebase y otras dependencias
        mavenCentral()  // Repositorio Maven Central
        gradlePluginPortal()  // Repositorio de plugins de Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)  // Se prefiere configuración en settings.gradle.kts
    repositories {
        google()  // Repositorio de Google para Firebase y otras dependencias
        mavenCentral()  // Repositorio Maven Central
    }
}

rootProject.name = "LacteosDisanaApp"
include(":app")
