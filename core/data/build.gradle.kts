plugins {
    alias(libs.plugins.runsphere.android.library)
    alias(libs.plugins.runsphere.jvm.ktor)
}

android {
    namespace = "com.twugteam.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.bundles.koin)
}