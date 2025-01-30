package com.twugteam.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility : LoginAction
    data object OnRegisterClick : LoginAction
    data object OnLoginClick: LoginAction
}