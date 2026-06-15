@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Secal"

// --- App ---
include(":app")

// --- Core katmanı ---
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:designsystem")
include(":core:ui")

// --- Feature katmanı ---
include(":feature:auth")
include(":feature:home")
include(":feature:catalog")
include(":feature:cart")
include(":feature:order")
include(":feature:seller")
include(":feature:profile")
include(":feature:reviews")
