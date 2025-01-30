package com.twugteam.auth.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class LoginResponse(
    val refreshToken: String,
    val accessToken: String,
    val accessTokenExpirationTimestamp: Long,
    val userId: String
)
