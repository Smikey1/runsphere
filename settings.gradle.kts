pluginManagement {
    includeBuild("build-logic")
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

// For --> Unable to make progress running work
/*
      - Ordinal groups:
          - group 0 entry nodes: [:build-logic:convention:clean (SHOULD_RUN)]
          - group 1 entry nodes: [:build-logic:convention:assemble (EXECUTED)]
          - group 2 entry nodes: [:build-logic:convention:testClasses (SHOULD_RUN)]
  - Workers waiting for work: 23
  - Stopped workers: 1

 */
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))


// TODO: typesafe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "RunSphere"
include(":app")

include(":core:data")
include(":core:domain")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:database")

include(":auth:data")
include(":auth:domain")
include(":auth:presentation")

include(":run:data")
include(":run:domain")
include(":run:presentation")
include(":run:location")
include(":run:network")
include(":core:test")
