package com.twugteam.run.presentation.active_run

import com.twugteam.core.domain.location.Location
import com.twugteam.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    // timer
    val elapsedTime: Duration = Duration.ZERO,
    val runData: RunData = RunData(),
    val shouldTrack: Boolean = false,
    val hasStartedRunningAlready: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false, // show progress on saving run
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false
)