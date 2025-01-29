package com.twugteam.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Icon
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
import com.twugteam.auth.domain.PasswordValidationState
import com.twugteam.auth.domain.UserDataValidator
import com.twugteam.auth.presentation.R
import com.twugteam.core.presentation.designsystem.CheckIcon
import com.twugteam.core.presentation.designsystem.CrossIcon
import com.twugteam.core.presentation.designsystem.EmailIcon
import com.twugteam.core.presentation.designsystem.Poppins
import com.twugteam.core.presentation.designsystem.RunSphereDarkRed
import com.twugteam.core.presentation.designsystem.RunSphereGray
import com.twugteam.core.presentation.designsystem.RunSphereGreen
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.components.GradientBackground
import com.twugteam.core.presentation.designsystem.components.RunSphereActionButton
import com.twugteam.core.presentation.designsystem.components.RunSpherePasswordTextField
import com.twugteam.core.presentation.designsystem.components.RunSphereTextField
import com.twugteam.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onLoginClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>()
) {
    val context = LocalContext.current
    val keyBoaController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when(event) {
            is RegisterEvent.Error -> {
                keyBoaController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_SHORT).show()
            }
            RegisterEvent.RegistrationSuccess -> {
                Toast.makeText(context, context.getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                onSuccessfulRegistration()
            }
        }
    }
    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    style = TextStyle(
                        color = RunSphereGray,
                        fontFamily = Poppins,
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.login),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
//                        .clickable(
//                            enabled = true,
//                            onClick = {
//                                onAction(RegisterAction.OnLoginClick)
//                            }
//                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onAction(RegisterAction.OnLoginClick)
                                }
                            )
                        }
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            RunSphereTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) CheckIcon else null,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                additionalInfo = stringResource(R.string.must_be_a_valid_email),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            RunSpherePasswordTextField(
                state = state.password,
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordRequirement(
                text = stringResource(
                    R.string.at_lest_x_characters,
                    UserDataValidator.MIN_PASSWORD_LENGTH
                ),
                isValid = state.passwordValidationState.hasMinimumLength
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.at_lest_one_number),
                isValid = state.passwordValidationState.hasNumber
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.contains_lowercase_character),
                isValid = state.passwordValidationState.hasLowercaseCharacter
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.contains_uppercase_character),
                isValid = state.passwordValidationState.hasUppercaseCharacter
            )
            Spacer(modifier = Modifier.height(32.dp))

            RunSphereActionButton(
                text = stringResource(id = R.string.register),
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                }
            )
        }
    }
}

@Composable
private fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) CheckIcon else CrossIcon,
            contentDescription = null,
            tint = if (isValid) RunSphereGreen else RunSphereDarkRed,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RunSphereTheme {
        RegisterScreen(
            state = RegisterState(
                passwordValidationState = PasswordValidationState(
                    hasNumber = true,
                    hasLowercaseCharacter = true,
                    hasMinimumLength = true,
                    hasUppercaseCharacter = true
                ),
                canRegister = false
            ),
            onAction = {},
        )
    }
}