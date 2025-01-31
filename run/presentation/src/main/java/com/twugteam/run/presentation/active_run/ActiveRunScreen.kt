package com.twugteam.run.presentation.active_run

import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.components.RunSphereScaffold
import com.twugteam.run.presentation.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel<ActiveRunViewModel>()
) {

}

@Composable
fun ActiveRunScreenScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    RunSphereScaffold(
        gradientEnabled = false
    ) {

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