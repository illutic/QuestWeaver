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

rootProject.name = "Quest Weaver"
include(":app")
include(":core:ui")
include(":core:nearby")
include(":core:domain")
include(":core:navigation")
include(":app:feature:onboarding")
include(":core:data")
include(":app:feature:home")
include(":app:feature:permissions")
include(":app:feature:user")
include(":app:feature:settings")
include(":app:feature:join_game")
include(":app:common:ui")
include(":app:feature:host_game")
include(":core:common:data")
