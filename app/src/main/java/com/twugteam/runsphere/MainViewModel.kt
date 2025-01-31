package com.twugteam.runsphere

import androidx.lifecycle.ViewModel
import com.twugteam.core.domain.SessionStorage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    sessionStorage: SessionStorage
) : ViewModel() {

    var state by mutableStateOf(MainState())
        private set


    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)
            state = state.copy(isLoggedInPreviously = sessionStorage.getAuthInto() != null)
            state = state.copy(isCheckingAuth = false)
        }
    }


}