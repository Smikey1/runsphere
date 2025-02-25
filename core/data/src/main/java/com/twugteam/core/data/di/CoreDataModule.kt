package com.twugteam.core.data.di

import com.twugteam.core.data.auth.EncryptedSessionStorage
import com.twugteam.core.data.networking.HttpClientFactory
import com.twugteam.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val coreDataModule = module {

    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()


}