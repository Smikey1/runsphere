package com.twugteam.run.di

import com.twugteam.core.domain.run.RemoteRunDataSource
import com.twugteam.run.network.KtorRunRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRunRemoteDataSource).bind<RemoteRunDataSource>()
}