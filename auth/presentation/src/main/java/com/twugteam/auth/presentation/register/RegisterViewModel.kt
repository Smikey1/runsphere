package com.twugteam.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twugteam.auth.domain.AuthRepository
import com.twugteam.auth.domain.UserDataValidator
import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.Result
import com.twugteam.core.presentation.ui.UiText
import com.twugteam.core.presentation.ui.asUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.twugteam.auth.presentation.R

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val repository: AuthRepository
) : ViewModel() {

    /*
        Two options available: But we can use any for state management
        1) Compose State : It is perfectly fine just to exposed to UI since that is what exactly need in UI
        2) State Flow: It is used when you need reactivity. If you need your state automatically react to change
        the we need to use flows for reactivity
     */

    // this is compose state
    var state by mutableStateOf(RegisterState())
        private set

    private val _email = snapshotFlow { state.email.text }
    private val _password = snapshotFlow { state.password.text }

    private var _eventChannel = Channel<RegisterEvent>()
    val events = _eventChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            _email.onEach { email ->
                val validEmail = userDataValidator.isValidEmail(email.toString())
                state = state.copy(
                    isEmailValid = validEmail,
                    canRegister = validEmail && state.passwordValidationState.isPasswordValid && !state.isRegistering
                )
            }.launchIn(viewModelScope)

            _password.onEach { password ->
                val passwordValidationState =
                    userDataValidator.validatePassword(password.toString())
                state = state.copy(
                    passwordValidationState = passwordValidationState,
                    canRegister = state.isEmailValid && passwordValidationState.isPasswordValid && !state.isRegistering
                )
            }.launchIn(viewModelScope)
        }
    }


    fun onAction(registerAction: RegisterAction) {
        when (registerAction) {
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            else -> Unit
        }
    }

    fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = repository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isRegistering = false)
            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        _eventChannel.send(RegisterEvent.Error(UiText.StringResource(R.string.error_email_exist)))
                    } else {
                        _eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    _eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }

}

