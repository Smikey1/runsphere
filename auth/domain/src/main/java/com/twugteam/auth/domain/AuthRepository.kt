package com.twugteam.auth.domain

import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult


interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}