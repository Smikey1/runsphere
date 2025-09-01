
import com.twugteam.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidJunit5ConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("runsphere.jvm.junit5")
            pluginManager.apply("de.mannodermaus.android-junit5")
            dependencies {
                "androidTestImplementation"(libs.findLibrary("junit5.api").get())
                "androidTestImplementation"(libs.findLibrary("junit5.params").get())
                "androidTestRuntimeOnly"(libs.findLibrary("junit5.engine").get())
                "androidTestImplementation"(libs.findLibrary("junit5.android.test.compose").get())

                // this screens need to access Android Manifest file. but in case of debugging we might run any screen,
                // and this dependency make sure it will provide the isolate manifest for testing
                "debugImplementation"(libs.findLibrary("androidx.compose.ui.test.manifest").get())

                "androidTestImplementation"(libs.findLibrary("assertk").get())
                "androidTestImplementation"(libs.findLibrary("coroutines.test").get())
                "androidTestImplementation"(libs.findLibrary("turbine").get())
            }
        }
    }
}