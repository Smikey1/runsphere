@file:OptIn(InternalSerializationApi::class)

package com.twugteam.auth.data

import com.twugteam.auth.domain.AuthRepository
import com.twugteam.core.data.auth.EncryptedSessionStorage
import com.twugteam.core.data.networking.post
import com.twugteam.core.domain.AuthInfo
import com.twugteam.core.domain.SessionStorage
import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import com.twugteam.core.domain.util.Result
import com.twugteam.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import kotlinx.serialization.InternalSerializationApi

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success){
            sessionStorage.setAuthInfo(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "register",
            body = RegisterRequest(email = email, password = password)
        )
    }
}