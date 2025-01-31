package com.twugteam.run.presentation.active_run

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.twugteam.run.presentation.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ActiveRunViewModel : ViewModel() {

    var state by mutableStateOf(ActiveRunState())
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    init {

    }

    fun onAction(action: ActiveRunAction) {

    }
}