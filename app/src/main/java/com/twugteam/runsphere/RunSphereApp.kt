package com.twugteam.runsphere

import android.app.Application
import com.twugteam.auth.data.di.authDataModule
import com.twugteam.auth.presentation.di.authViewModelModule
import com.twugteam.core.data.di.coreDataModule
import com.twugteam.core.database.di.databaseModule
import com.twugteam.run.di.networkModule
import com.twugteam.run.location.di.locationModule
import com.twugteam.run.presentation.di.runPresentationModule
import com.twugteam.runsphere.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class RunSphereApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RunSphereApp)
            modules(
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkModule
            )
        }
    }
}