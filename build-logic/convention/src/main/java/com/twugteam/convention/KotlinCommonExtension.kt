package com.twugteam.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// This function can be applied to android application module and android library module
// but not to kotlin jvm library module
internal fun Project.configureKotlinAndroidExtension(commonExtension: CommonExtension<*,*,*,*,*,*>){
    // this is similar to android section/block of build.gradle.kts file
    commonExtension.apply {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

        compileOptions{
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

    }
    configureKotlinJvm()

    dependencies {
        //TODO: DeSugar
//         This library make Local Date, Time instance compatible down to android api level 21 from 26.
//         It will do some compile time magic to compile the Date/Time code to lower version as well.
        "coreLibraryDesugaring"(libs.findLibrary("desugar.jdk.libs").get())
    }

}

internal fun Project.configureKotlinLibraryJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    configureKotlinJvm()
}


// This function can be applied to kotlin jvm library module
// but not to android application module and android library module
private fun Project.configureKotlinJvm(){
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        }
//        kotlinOptions {
//            jvmTarget = JavaVersion.VERSION_11.toString()
//        }
    }

}