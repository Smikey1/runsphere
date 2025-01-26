package com.twugteam.auth.presentation.register

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {

    /*
        Two options available: But we can use any for state management
        1) Compose State : It is perfectly fine just to exposed to UI since that is what exactly need in UI
        2) State Flow: It is used when you need reactivity. If you need your state automatically react to change
        the we need to use flows for reactivity
     */

    // this is compose state
    var state by mutableStateOf(RegisterState())
        private set

    fun onAction(registerAction: RegisterAction){
        when(registerAction){
            RegisterAction.OnLoginClick -> TODO()
            RegisterAction.OnRegisterClick -> TODO()
            RegisterAction.OnTogglePasswordVisibilityClick -> TODO()
        }
    }
}

