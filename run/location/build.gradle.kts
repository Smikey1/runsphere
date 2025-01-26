plugins {
    alias(libs.plugins.runsphere.android.library)
}

android {
    namespace = "com.twugteam.run.location"
}

dependencies {

    implementation(libs.androidx.core.ktx)

//    implementation(libs.androidx.compose.ui)
//    implementation(libs.androidx.compose.ui.tooling.preview)
//    implementation(libs.androidx.compose.ui.graphics)
//    implementation(libs.androidx.compose.material3)
//    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}