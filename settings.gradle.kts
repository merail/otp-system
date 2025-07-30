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

rootProject.name = "otp-system"
include(":app")
include(":design")
include(":core")
include(":navigation")
include(":navigation:domain")
include(":navigation:graph")
include(":email")
include(":auth")
include(":auth:api")
include(":auth:impl")
include(":otp")
include(":password")
include(":home")
