plugins {
    alias(libs.plugins.runsphere.jvm.library)
}


//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
//}

dependencies {
    implementation(projects.core.domain)
}
