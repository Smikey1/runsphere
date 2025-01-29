package com.twugteam.auth.data

import com.twugteam.auth.domain.AuthRepository
import com.twugteam.core.data.networking.post
import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import io.ktor.client.HttpClient
import kotlinx.serialization.InternalSerializationApi

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {
    @OptIn(InternalSerializationApi::class)
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