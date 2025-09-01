plugins {
    alias(libs.plugins.runsphere.android.library)
    alias(libs.plugins.runsphere.android.room)
}

android {
    namespace = "com.twugteam.core.database"
}

dependencies {
    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)

    implementation(libs.bundles.koin)

}