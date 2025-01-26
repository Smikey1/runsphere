package com.twugteam.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project


fun DependencyHandlerScope.addUILayerDependencies(gradleProject: Project){
    "implementation"(project(":core:presentation:ui"))
    "implementation"(project(":core:presentation:designsystem"))

    "implementation"(gradleProject.libs.findBundle("koin-compose").get())
    "implementation"(gradleProject.libs.findBundle("compose").get())
    "debugImplementation"(gradleProject.libs.findBundle("compose-debug").get())
    "androidTestImplementation"(gradleProject.libs.findLibrary("androidx.compose.ui.test.junit4").get())
}