package com.twugteam.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }
//        composeOptions {
//            kotlinCompilerExtensionVersion = libs.findVersion("kotlin").get().toString()
//        }
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            val toolingLibraryPreview =
                libs.findLibrary("androidx.compose.ui.tooling.preview").get()

            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(toolingLibraryPreview)
        }
    }
}

