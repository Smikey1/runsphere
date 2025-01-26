plugins {
    alias(libs.plugins.runsphere.android.library.compose)
}

android {
    namespace = "com.twugteam.core.presentation.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //TODO: api vs implementation in dependency
//    api is here for a reason that if any other module depend on this module i.e. designsystem.
//    for example ui module implement designsystem, then the implementation keyword doesn't include all the dependency
//    available on material3 to ui module. It only include design system. so by doing api keyword, it make sure that it will
//    include material3 library along with this module i.e design system to ui module.

    api(libs.androidx.compose.material3)

}