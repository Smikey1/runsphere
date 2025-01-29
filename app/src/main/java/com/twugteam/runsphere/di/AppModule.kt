package com.twugteam.runsphere.di

import com.twugteam.auth.data.di.authDataModule
import com.twugteam.auth.presentation.di.authViewModelModule
import org.koin.dsl.module


val appModule = module {
    authDataModule
    authViewModelModule
}