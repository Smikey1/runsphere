package com.twugteam.run.presentation.run_overview

import com.twugteam.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartRunClick : RunOverviewAction
    data object OnAnalyticsClick : RunOverviewAction
    data object OnLogoutClick : RunOverviewAction
    data class DeleteRun(val runUi: RunUi) : RunOverviewAction
}