package com.twugteam.run.presentation.run_overview

sealed interface RunOverviewAction {
    data object OnStartRunClick : RunOverviewAction
    data object OnResumeRunClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
    data object OnLogoutClick : RunOverviewAction
}