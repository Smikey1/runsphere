package com.twugteam.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClick : ActiveRunAction
    data object OnFinishedRunClick : ActiveRunAction
    data object OnResumeRunClick : ActiveRunAction
    data object OnBackClick : ActiveRunAction
}