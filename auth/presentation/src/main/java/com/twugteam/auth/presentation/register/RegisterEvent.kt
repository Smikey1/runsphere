package com.twugteam.auth.presentation.register

import com.twugteam.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data class Error(val error: UiText) : RegisterEvent
    data object RegistrationSuccess : RegisterEvent
}
