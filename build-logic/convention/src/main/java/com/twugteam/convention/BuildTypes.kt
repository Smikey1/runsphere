package com.twugteam.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(commonExtension: CommonExtension<*, *, *, *, *, *>,
                                         extensionBuildType: ExtensionBuildType){
    commonExtension.run {

        // we need to enable buildConfig to use buildFeature
        buildFeatures {
            buildConfig=true
        }

        val apiKey = gradleLocalProperties(rootDir,rootProject.providers).getProperty("API_KEY")
        when(extensionBuildType){
            ExtensionBuildType.APPLICATION_EXTENSION ->{
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey)
                        }

                        release {
                            configureReleaseBuildType(commonExtension,apiKey)
                        }

                    }
                }
            }
            ExtensionBuildType.LIBRARY_EXTENSION -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey)
                        }

                        release {
                            configureReleaseBuildType(commonExtension,apiKey)
                        }

                    }
                }
            }
        }
    }

}

private fun BuildType.configureDebugBuildType(apiKey:String){
    buildConfigField("String","API_KEY","\"$apiKey\"")
    buildConfigField("String","BASE_URL","\"http://192.168.1.109:8080\"")
}
private fun BuildType.configureReleaseBuildType(commonExtension: CommonExtension<*, *, *, *, *, *>,
                                                apiKey:String){
    buildConfigField("String","API_KEY","\"$apiKey\"")
    buildConfigField("String","BASE_URL","\"http://192.168.1.109:8080\"")
    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}