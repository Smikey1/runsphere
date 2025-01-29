plugins {
    alias(libs.plugins.runsphere.android.library)
    alias(libs.plugins.runsphere.jvm.ktor)
}

android {
    namespace = "com.twugteam.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}