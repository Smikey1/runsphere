package com.twugteam.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.twugteam.core.domain.run.RunRepository

class RunOverviewViewModel(
    private val runRepository: RunRepository
) : ViewModel() {

    var state by mutableStateOf(RunOverviewState())
        private set

    init {

    }

    fun onAction(action: RunOverviewAction) {

    }
}