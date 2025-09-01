package com.twugteam.core.data.di

import com.twugteam.core.data.auth.EncryptedSessionStorage
import com.twugteam.core.data.networking.HttpClientFactory
import com.twugteam.core.data.run.OfflineFirstRunRepository
import com.twugteam.core.domain.SessionStorage
import com.twugteam.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val coreDataModule = module {

    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}