package com.twugteam.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twugteam.core.domain.location.Location
import com.twugteam.core.domain.run.Run
import com.twugteam.run.domain.LocationDataCalculator
import com.twugteam.run.domain.RunningTracker
import com.twugteam.run.presentation.active_run.service.ActiveRunService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
) : ViewModel() {

    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = ActiveRunService.isNotificationServiceActive && runningTracker.isTrackingActively.value,
            hasStartedRunningAlready = ActiveRunService.isNotificationServiceActive
        )
    )
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(value = false)

    private val isTracking =
        combine(shouldTrack, hasLocationPermission) { shouldTrack, hasLocationPermission ->
            shouldTrack && hasLocationPermission
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    init {
        hasLocationPermission.onEach { hasPermission ->
            if (hasPermission) {
                runningTracker.startObservingLocation()
            } else {
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)
        }.launchIn(viewModelScope)

        runningTracker.currentLocation
            .onEach {
                state = state.copy(currentLocation = it?.location)
            }.launchIn(viewModelScope)

        runningTracker.runData
            .onEach {
                state = state.copy(runData = it)
            }.launchIn(viewModelScope)

        runningTracker.elapsedTime
            .onEach {
                state = state.copy(elapsedTime = it)
            }.launchIn(viewModelScope)
    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            ActiveRunAction.OnFinishedRunClick -> {
                state = state.copy(
                    isRunFinished = true,
                    // for showing progress indicator
                    isSavingRun = true
                )
            }

            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(shouldTrack = true)
            }

            ActiveRunAction.OnBackClick -> {
                state = state.copy(shouldTrack = false)
            }

            ActiveRunAction.OnToggleRunClick -> {
                state =
                    state.copy(hasStartedRunningAlready = true, shouldTrack = !state.shouldTrack)
            }

            is ActiveRunAction.SubmitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(showLocationRationale = action.showLocationRationale)
            }

            is ActiveRunAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(showNotificationRationale = action.showNotificationRationale)
            }

            ActiveRunAction.DismissRationaleDialog -> {
                state = state.copy(
                    showNotificationRationale = false,
                    showLocationRationale = false
                )
            }

            is ActiveRunAction.OnRunProcessed -> {
                finishRun(action.mapPictureByte)
            }

            else -> Unit
        }
    }

    private fun finishRun(imageByte: ByteArray) {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }
        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUtc = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeter = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(locations),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(locations),
                mapPictureUrl = null,
            )

            // call repository

            runningTracker.finishRun()
            state = state.copy(isSavingRun = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isNotificationServiceActive) {
            runningTracker.startObservingLocation()
        }
    }
}