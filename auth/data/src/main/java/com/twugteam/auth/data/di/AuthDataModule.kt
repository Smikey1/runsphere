package com.twugteam.auth.data.di

import com.twugteam.auth.data.AuthRepositoryImpl
import com.twugteam.auth.data.EmailPatternValidator
import com.twugteam.auth.domain.AuthRepository
import com.twugteam.auth.domain.PatternValidator
import com.twugteam.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val authDataModule = module {
    single<PatternValidator> { EmailPatternValidator }
    singleOf(::UserDataValidator)

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}