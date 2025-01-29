package com.twugteam.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.twugteam.auth.domain.PasswordValidationState

data class RegisterState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,

    // This canRegister field is used to enabled or disable our register button
    val canRegister: Boolean = false
)