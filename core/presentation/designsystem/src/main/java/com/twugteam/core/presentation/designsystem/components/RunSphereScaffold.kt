package com.twugteam.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.twugteam.core.presentation.designsystem.RunSphereTheme


@Composable
fun RunSphereScaffold(
    modifier: Modifier = Modifier,
    gradientEnabled: Boolean = true,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center
    ) {
        if (gradientEnabled) {
            GradientBackground {
                content(it)
            }
        } else {
            content(it)
        }
    }
}


@Preview
@Composable
fun RunSphereScaffoldPreview() {
    RunSphereTheme {
        RunSphereScaffold(
            modifier = Modifier,
            gradientEnabled = true
        ) {

        }
    }
}