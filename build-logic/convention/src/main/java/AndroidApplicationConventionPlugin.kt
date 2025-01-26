import com.android.build.api.dsl.ApplicationExtension
import com.twugteam.convention.ExtensionBuildType
import com.twugteam.convention.configureBuildTypes
import com.twugteam.convention.configureKotlinAndroidExtension
import com.twugteam.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            // plugin section of build.gradle
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            // android section(Referred as BasedApplicationModuleExtension or CommonExtension) of build.gradle
            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()

                }
                configureKotlinAndroidExtension(this)
                configureBuildTypes(this,ExtensionBuildType.APPLICATION_EXTENSION)
            }
        }
    }
}

