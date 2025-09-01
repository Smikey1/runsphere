package com.twugteam.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {
    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasNumber: Boolean = password.any { it.isDigit() }
        val hasUppercaseCharacter = password.any { it.isUpperCase() }
        val hasLowercaseCharacter = password.any { it.isLowerCase() }
        val hasMinimumLength = password.length >= MIN_PASSWORD_LENGTH

        return PasswordValidationState(
            hasMinimumLength = hasMinimumLength,
            hasUppercaseCharacter = hasUppercaseCharacter,
            hasLowercaseCharacter = hasLowercaseCharacter,
            hasNumber = hasNumber
        )
    }

}