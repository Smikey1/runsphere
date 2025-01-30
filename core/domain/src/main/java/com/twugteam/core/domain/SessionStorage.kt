package com.twugteam.core.domain

interface SessionStorage {
    suspend fun getAuthInto(): AuthInfo?
    suspend fun setAuthInfo(authInfo: AuthInfo?)
}