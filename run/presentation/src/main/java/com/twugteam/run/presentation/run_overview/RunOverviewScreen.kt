@file:OptIn(ExperimentalMaterial3Api::class)

package com.twugteam.run.presentation.run_overview

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twugteam.core.presentation.designsystem.AnalyticsIcon
import com.twugteam.core.presentation.designsystem.LogoIcon
import com.twugteam.core.presentation.designsystem.LogoutIcon
import com.twugteam.core.presentation.designsystem.R
import com.twugteam.core.presentation.designsystem.RunIcon
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.components.RunSphereFloatingActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereScaffold
import com.twugteam.core.presentation.designsystem.components.RunSphereTopAppBar
import com.twugteam.core.presentation.designsystem.components.utils.DropdownItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
) {
    val viewModel: RunOverviewViewModel = koinViewModel<RunOverviewViewModel>()

}

@Composable
private fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = topAppBarState)
    RunSphereScaffold(
        topAppBar = {
            RunSphereTopAppBar(
                showBackButton = false,
                title = stringResource(R.string.run_sphere),
                scrollBehavior = scrollBehavior,
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                menuItems = listOf(
                    DropdownItem(icon = AnalyticsIcon, title = stringResource(R.string.analytics)),
                    DropdownItem(icon = LogoutIcon, title = stringResource(R.string.logout))
                ),
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnAnalyticsClick)
                    }
                },
                modifier = Modifier
            )
        },
        floatingActionButton = {
            RunSphereFloatingActionButton(
                icon = RunIcon,
                onClick = {
                    onAction(RunOverviewAction.OnStartRunClick)
                }
            )
        }
    ) { paddingValues ->

    }
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RunSphereTheme {
        RunOverviewScreen(
            state = RunOverviewState(),
            onAction = {}
        )
    }
}

