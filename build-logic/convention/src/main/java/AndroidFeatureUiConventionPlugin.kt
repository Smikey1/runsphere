
import com.twugteam.convention.addUILayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


// It will be use every presentation layer of feature module
class AndroidFeatureUiConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                // because it will be used for presentation layer
                apply("runsphere.android.library.compose")
            }
            dependencies {
                addUILayerDependencies(target)
            }
        }
    }
}