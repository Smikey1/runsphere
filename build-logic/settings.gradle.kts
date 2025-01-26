dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    //TODO: Referencing Version Catalog in build-logic
//    Here, we need to reference the version catalog of existing main project.
    versionCatalogs{
        create("libs"){
            from(files("../gradle/libs.versions.toml"))  // ../ --> parent/root folder
        }
    }
}

rootProject.name = "build-logic"
include(":convention")