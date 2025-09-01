plugins {
    alias(libs.plugins.runsphere.jvm.library)
    alias(libs.plugins.runsphere.jvm.junit5)
}


//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
//}

dependencies {
    implementation(projects.core.domain)
    testImplementation(libs.junit.jupiter)
}
