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
include(":core:domain")
include(":core:navigation")
include(":app:feature:onboarding")
include(":core:data")
include(":app:feature:home")
include(":app:feature:permissions")
include(":app:feature:user")
include(":app:feature:settings")
include(":app:feature:joingame")
include(":app:common:ui")
include(":app:feature:hostgame")
include(":core:common:data")
include(":app:feature:game")
include(":app:feature:game:home")
include(":app:feature:game:chat")
include(":app:feature:game:ai")
