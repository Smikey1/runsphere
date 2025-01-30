package com.twugteam.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twugteam.auth.presentation.R
import com.twugteam.core.presentation.designsystem.EmailIcon
import com.twugteam.core.presentation.designsystem.Poppins
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.components.GradientBackground
import com.twugteam.core.presentation.designsystem.components.RunSphereActionButton
import com.twugteam.core.presentation.designsystem.components.RunSpherePasswordTextField
import com.twugteam.core.presentation.designsystem.components.RunSphereTextField
import com.twugteam.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onSuccessfulLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {
    val context = LocalContext.current
    val keyBoardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                keyBoardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }

            LoginEvent.LoginSuccess -> {
                keyBoardController?.hide()
                Toast.makeText(context, R.string.login_successful, Toast.LENGTH_SHORT).show()
                onSuccessfulLogin()
            }
        }
    }
    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> onRegisterClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 32.dp)
                    .padding(bottom = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.hi_there),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.runsphere_welcome_text),
                    fontSize = 12.sp,
                    color =
                    MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(48.dp))
                RunSphereTextField(
                    state = state.email,
                    startIcon = EmailIcon,
                    endIcon = null,
                    hint = stringResource(R.string.example_email),
                    title = stringResource(R.string.email),
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                RunSpherePasswordTextField(
                    state = state.password,
                    title = stringResource(R.string.password),
                    isPasswordVisible = state.isPasswordVisible,
                    hint = stringResource(R.string.password),
                    onTogglePasswordVisibilityClick = {
                        onAction(LoginAction.OnTogglePasswordVisibility)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(32.dp))
                RunSphereActionButton(
                    text = stringResource(R.string.login),
                    enabled = state.canLogin && !state.isLoggingIn,
                    isLoading = state.isLoggingIn,
                    onClick = {
                        onAction(LoginAction.OnLoginClick)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.dont_have_an_account),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = Poppins,
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.register),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onAction(LoginAction.OnRegisterClick)
                                }
                            )
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    RunSphereTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}
