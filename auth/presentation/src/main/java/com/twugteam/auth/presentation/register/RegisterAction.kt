package com.twugteam.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnLoginClick: RegisterAction
//    data object OnBackClick: RegisterAction
}