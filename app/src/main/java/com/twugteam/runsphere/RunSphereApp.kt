package com.twugteam.runsphere

import android.app.Application
import com.twugteam.auth.data.di.authDataModule
import com.twugteam.auth.presentation.di.authViewModelModule
import com.twugteam.core.data.di.coreDataModule
import com.twugteam.run.location.di.locationModule
import com.twugteam.run.presentation.di.runViewModelModule
import com.twugteam.runsphere.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class RunSphereApp : Application() {
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
                runViewModelModule,
                locationModule
            )
        }
    }
}