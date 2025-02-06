@file:OptIn(ExperimentalMaterial3Api::class)

package com.twugteam.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twugteam.core.presentation.designsystem.RunSphereTheme
import com.twugteam.core.presentation.designsystem.StartIcon
import com.twugteam.core.presentation.designsystem.StopIcon
import com.twugteam.core.presentation.designsystem.components.RunSphereActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereDialog
import com.twugteam.core.presentation.designsystem.components.RunSphereFloatingActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereOutlineActionButton
import com.twugteam.core.presentation.designsystem.components.RunSphereScaffold
import com.twugteam.core.presentation.designsystem.components.RunSphereTopAppBar
import com.twugteam.run.presentation.R
import com.twugteam.run.presentation.active_run.components.RunDataCard
import com.twugteam.run.presentation.active_run.maps.TrackerMap
import com.twugteam.run.presentation.utils.hasLocationPermission
import com.twugteam.run.presentation.utils.hasNotificationPermission
import com.twugteam.run.presentation.utils.shouldShowLocationPermissionRationale
import com.twugteam.run.presentation.utils.shouldShowPostNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel


@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel<ActiveRunViewModel>()
) {
    ActiveRunScreenScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ActiveRunScreenScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        val hasCoarseLocationPermission =
            permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasPostNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            permission[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowPostNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasPostNotificationPermission,
                showNotificationRationale = showNotificationRationale
            )
        )
    }

    // for very first time user open the app and if permission is not granted
    LaunchedEffect(true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowPostNotificationPermissionRationale()


        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale
            )
        )

        if (!showNotificationRationale && !showLocationRationale) {
            permissionLauncher.requestRunSpherePermission(context)
        }

    }
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
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .padding(paddingValues)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = {},
                modifier = Modifier.fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }

    if (!state.shouldTrack && state.hasStartedRunningAlready) {
        RunSphereDialog(
            title = stringResource(id = R.string.running_is_paused),
            description = stringResource(id = R.string.resume_or_finish_run),
            onDismiss = {
                onAction(ActiveRunAction.OnResumeRunClick)
            },
            primaryActionButton = {
                RunSphereActionButton(
                    text = stringResource(R.string.resume),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            },
            secondaryActionButton = {
                RunSphereOutlineActionButton(
                    text = stringResource(R.string.finish),
                    isLoading = state.isSavingRun,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishedRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        RunSphereDialog(
            title = stringResource(R.string.permission_required),
            onDismiss = {
                // Don't allow user to dismiss the permission dialog in case of permission things
            },
            primaryActionButton = {
                RunSphereOutlineActionButton(
                    text = stringResource(R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRunSpherePermission(context)
                    }
                )
            },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> stringResource(R.string.request_location_and_notification_permission)
                state.showLocationRationale -> stringResource(R.string.request_location_permission)
                else -> stringResource(R.string.request_notification_permission)

            }.toString(),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRunSpherePermission(context: Context) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission =
        if (Build.VERSION.SDK_INT >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermission + notificationPermission)
        }

        !hasLocationPermission -> {
            launch(locationPermission)
        }

        !hasNotificationPermission -> launch(notificationPermission)
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