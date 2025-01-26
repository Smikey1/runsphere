plugins {
    alias(libs.plugins.runsphere.android.application.compose)
    alias(libs.plugins.runsphere.jvm.ktor)

}

android {
    namespace = "com.twugteam.runsphere"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto --> for encrypted shared preferences
    implementation(libs.androidx.security.crypto.ktx)

    implementation(libs.bundles.koin)

    //TODO: api Info
    // if a module include app module except dynamic feature module, then the module also include
    // libs.core module (because of api) along with app module
    api(libs.core)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Location
    implementation(libs.google.android.gms.play.services.location)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Timber
    implementation(libs.timber)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.database)

    implementation(projects.auth.data)
    implementation(projects.auth.domain)
    implementation(projects.auth.presentation)

    implementation(projects.run.data)
    implementation(projects.run.domain)
    implementation(projects.run.presentation)
    implementation(projects.run.location)
    implementation(projects.run.network)

}