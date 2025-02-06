@file:OptIn(ExperimentalCoroutinesApi::class)

package com.twugteam.run.domain

import com.twugteam.core.domain.Timer
import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {

    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val _isTrackingRunActively = MutableStateFlow(false)
    val isTrackingActively = _isTrackingRunActively.asStateFlow()
    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observeLocation(1000L)
            } else flowOf()

        }.stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    init {
        _isTrackingRunActively
            .onEach { isTracking ->
                if (!isTracking) {
                    val newList = buildList<List<LocationTimestampWithAltitude>> {
                        addAll(runData.value.locations)
                        add(emptyList<LocationTimestampWithAltitude>())
                    }.toList()
                    _runData.update {
                        it.copy(locations = newList)
                    }
                }
            }
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                // this on-each block won't called for empty flow,

                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        currentLocation
            .filterNotNull()
            .combineTransform(_isTrackingRunActively) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }
            .zip(_elapsedTime) { location, elapsedTime ->
                LocationTimestampWithAltitude(
                    location,
                    elapsedTime
                )
            }
            .onEach { newLocation ->
                val currentLocation = runData.value.locations
                val lastLocationList = if (currentLocation.isNotEmpty()) {
                    currentLocation.last() + newLocation
                } else {
                    listOf(newLocation)
                }
                val newLocationList = currentLocation.replaceLast(lastLocationList)

                val distanceMeters = LocationDataCalculator.getTotalDistanceMeter(newLocationList)
                val distanceKm = distanceMeters / 1000.0
                val currentDuration = newLocation.durationTimestamp
                val avgSecondPerKm = if (distanceKm == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }
                _runData.update {
                    RunData(
                        distanceMeters = distanceMeters,
                        pace = avgSecondPerKm.seconds,
                        locations = newLocationList
                    )
                }
            }.launchIn(applicationScope)
    }


    fun setIsTracking(isTracking: Boolean) {
        this._isTrackingRunActively.value = isTracking
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (this.isEmpty()) {
        return listOf(replacement)
    }
    return this.dropLast(1) + listOf(replacement)
}