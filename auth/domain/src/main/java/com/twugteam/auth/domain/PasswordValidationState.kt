package com.twugteam.auth.domain

data class PasswordValidationState(
    val hasMinimumLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasUppercaseCharacter: Boolean = false,
    val hasLowercaseCharacter: Boolean = false
) {
    val isPasswordValid: Boolean
        get() = hasNumber && hasLowercaseCharacter && hasUppercaseCharacter && hasMinimumLength

}