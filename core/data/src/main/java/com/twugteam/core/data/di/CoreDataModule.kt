package com.twugteam.core.data.di

import com.twugteam.core.data.networking.HttpClientFactory
import org.koin.dsl.module


val coreDataModule = module {

    single {
        HttpClientFactory().build()
    }
}