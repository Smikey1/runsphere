package com.twugteam.run.presentation.run_overview

import com.twugteam.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
