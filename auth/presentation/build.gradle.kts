plugins {
    alias(libs.plugins.runsphere.android.feature.ui)
}

android {
    namespace = "com.twugteam.auth.presentation"
}

dependencies {

    //TODO: implementation(project(":auth:domain"))
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
    implementation(project(":auth:data"))
}

