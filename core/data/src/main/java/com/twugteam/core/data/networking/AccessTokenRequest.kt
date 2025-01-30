package com.twugteam.core.data.networking

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String
)
