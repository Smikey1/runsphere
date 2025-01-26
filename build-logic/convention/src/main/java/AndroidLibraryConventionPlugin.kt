import com.android.build.api.dsl.LibraryExtension
import com.twugteam.convention.ExtensionBuildType
import com.twugteam.convention.configureBuildTypes
import com.twugteam.convention.configureKotlinAndroidExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

// this is for data layer
class AndroidLibraryConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroidExtension(this)
                configureBuildTypes(this,ExtensionBuildType.LIBRARY_EXTENSION)

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "testImplementation"(kotlin("test"))
            }
        }
    }
}