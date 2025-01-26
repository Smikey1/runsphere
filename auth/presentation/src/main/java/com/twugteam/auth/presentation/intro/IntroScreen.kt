package com.twugteam.auth.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twugteam.core.presentation.designsystem.LogoIcon
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.components.GradientBackground
import com.twugteam.auth.presentation.R
import com.twugteam.core.presentation.designsystem.components.RunSphereActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereOutlineActionButton

@Composable
fun IntroScreenRoot(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    IntroScreen(onAction = { introAction ->
        when (introAction) {
            IntroAction.OnLoginClick -> onLoginClick()
            IntroAction.OnRegisterClick -> onRegisterClick()
        }
    })
}

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RunSphereLogoVertical()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_to_runsphere),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.runsphere_description),
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(32.dp))
            RunSphereOutlineActionButton(
                text = stringResource(id = R.string.login),
                isLoading = false,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onAction(IntroAction.OnLoginClick)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RunSphereActionButton(
                text = stringResource(id = R.string.register),
                isLoading = false,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onAction(IntroAction.OnRegisterClick)
                }
            )
        }
    }
}


@Composable
private fun RunSphereLogoVertical(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = LogoIcon,
            contentDescription = "RunSphere Logo",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.runsphere),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Preview
@Composable
private fun IntroScreenPreview() {
    RunSphereTheme {
        IntroScreen(
            onAction = {}
        )
    }
}



