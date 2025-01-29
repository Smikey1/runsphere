package com.twugteam.auth.domain

interface PatternValidator {
    fun matches(values: String): Boolean
}