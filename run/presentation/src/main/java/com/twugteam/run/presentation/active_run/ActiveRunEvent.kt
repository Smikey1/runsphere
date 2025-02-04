package com.twugteam.run.presentation.active_run

import com.twugteam.core.presentation.ui.UiText

sealed interface ActiveRunEvent {
    data object RunSaved : ActiveRunEvent
    data class Error(val error: UiText) : ActiveRunEvent
}