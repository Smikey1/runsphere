plugins {
    alias(libs.plugins.runsphere.jvm.library)
    alias(libs.plugins.runsphere.jvm.junit5)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
//    }
//}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.run.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.junit5.api)

    // for asynchronous functionality that runs with coroutines, flow, and heavily relying on dispatchers
    // and coroutine scope
    implementation(libs.coroutines.test)

}
