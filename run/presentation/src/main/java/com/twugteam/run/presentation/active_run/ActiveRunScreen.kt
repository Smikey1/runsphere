@file:OptIn(ExperimentalMaterial3Api::class)

package com.twugteam.run.presentation.active_run

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.StartIcon
import com.twugteam.core.presentation.designsystem.StopIcon
import com.twugteam.core.presentation.designsystem.components.RunSphereFloatingActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereScaffold
import com.twugteam.core.presentation.designsystem.components.RunSphereTopAppBar
import com.twugteam.run.presentation.R
import com.twugteam.run.presentation.active_run.components.RunDataCard
import org.koin.androidx.compose.koinViewModel


@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel<ActiveRunViewModel>()
) {
}

@Composable
private fun ActiveRunScreenScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    RunSphereScaffold(
        gradientEnabled = false,
        topAppBar = {
            RunSphereTopAppBar(
                showBackButton = true,
                onBackClick = {
                    onAction(ActiveRunAction.OnResumeRunClick)
                },
                title = stringResource(R.string.active_run)
            )
        },
        floatingActionButton = {
            RunSphereFloatingActionButton(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                contentDescription = if (state.shouldTrack) stringResource(R.string.pause_run) else stringResource(
                    R.string.start_run
                ),
                iconSize = 20.dp,
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClick)
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(paddingValues)
        ) {
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun ActiveRunScreenPreview() {
    RunSphereTheme {
        ActiveRunScreenScreen(
            state = ActiveRunState(),
            onAction = { }
        )
    }
}