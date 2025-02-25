package com.twugteam.runsphere.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

import androidx.security.crypto.MasterKey
import com.twugteam.runsphere.MainViewModel
import com.twugteam.runsphere.RunSphereApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    single<SharedPreferences> {
        EncryptedSharedPreferences(
            context = androidApplication(),
            fileName = "runsphere_pref",
            masterKey = MasterKey(androidApplication()),
            prefKeyEncryptionScheme = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            prefValueEncryptionScheme = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    single<CoroutineScope> {
        (androidApplication() as RunSphereApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}