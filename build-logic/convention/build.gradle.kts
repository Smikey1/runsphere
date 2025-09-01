plugins {
    `kotlin-dsl`
}

group ="com.twugteam.runsphere.buildlogic"


dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}


gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "runsphere.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "runsphere.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        // android library is for data layer
        register("androidLibrary") {
            id = "runsphere.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        // android library compose for presentation layer
        register("androidLibraryCompose") {
            id = "runsphere.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUi") {
            id = "runsphere.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }

        register("androidRoom") {
            id = "runsphere.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("jvmLibrary") {
            id = "runsphere.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("jvmKtor") {
            id = "runsphere.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }

        register("jvmJunit5") {
            id = "runsphere.jvm.junit5"
            implementationClass = "JvmJunit5ConventionPlugin"
        }

        register("androidJunit5") {
            id = "runsphere.android.junit5"
            implementationClass = "AndroidJunit5ConventionPlugin"
        }
    }
}
