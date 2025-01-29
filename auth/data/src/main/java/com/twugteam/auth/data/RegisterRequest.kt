package com.twugteam.auth.data

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class RegisterRequest(
    val email: String,
    val password: String
)
