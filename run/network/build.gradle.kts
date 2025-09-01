plugins {
    alias(libs.plugins.runsphere.android.library)
    alias(libs.plugins.runsphere.jvm.ktor)
}

android {
    namespace = "com.twugteam.run.network"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.bundles.koin)
}