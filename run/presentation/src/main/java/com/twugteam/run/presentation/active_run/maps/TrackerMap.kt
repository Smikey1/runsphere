@file:OptIn(MapsComposeExperimentalApi::class)

package com.twugteam.run.presentation.active_run.maps

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.awaitSnapshot
import com.twugteam.core.domain.location.Location
import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import com.twugteam.core.presentation.designsystem.RunIcon
import com.twugteam.run.presentation.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TrackerMap(
    isRunFinished: Boolean,
    currentLocation: Location?,
    locations: List<List<LocationTimestampWithAltitude>>,
    onSnapshot: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberMarkerState()
    val markerPositionLat by animateFloatAsState(
        targetValue = currentLocation?.lat?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )
    val markerPositionLong by animateFloatAsState(
        targetValue = currentLocation?.long?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    val markerPosition = remember(markerPositionLat, markerPositionLong) {
        LatLng(markerPositionLat.toDouble(), markerPositionLong.toDouble())
    }

    LaunchedEffect(markerPosition, isRunFinished) {
        if (!isRunFinished) {
            markerState.position = markerPosition
        }
    }

    // automatically animated camera to center position with current location
    LaunchedEffect(currentLocation, isRunFinished) {
        if (currentLocation != null && !isRunFinished) {
            val latLong = LatLng(currentLocation.lat, currentLocation.long)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(latLong, 17f)
            )
        }
    }
    var triggerCapture by remember { mutableStateOf(false) }
    var createSnapshotCoroutineJob: Job? = remember { null }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = mapStyle
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        modifier = if (isRunFinished) {
            modifier
                .width(300.dp)
                .aspectRatio(16 / 9f)
                .alpha(0f)
                .onSizeChanged {
                    if (it.width >= 300) {
                        triggerCapture = true
                    }
                }
        } else modifier
    ) {
        RunSpherePolyline(locations = locations)

        MapEffect(
            locations,
            isRunFinished,
            triggerCapture,
            createSnapshotCoroutineJob
        ) { googleMap ->
            if (isRunFinished && triggerCapture && createSnapshotCoroutineJob != null) {
                triggerCapture = false

                // From this boundsBuilder  --> google map will automatically adjust the zoom level
                // make all visible in snapshot
                val boundsBuilder = LatLngBounds.builder()
                locations.flatten().forEach { locationTimestamp ->
                    boundsBuilder.include(
                        LatLng(
                            locationTimestamp.locationWithAltitude.location.lat,
                            locationTimestamp.locationWithAltitude.location.long
                        )
                    )
                }
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        100
                    )
                )
                googleMap.setOnCameraIdleListener {
                    createSnapshotCoroutineJob?.cancel()
                    createSnapshotCoroutineJob = GlobalScope.launch {
                        // make sure the map is sharp and focused before taking the screenshot
                        // once camera comes to IDLE state from moving
                        delay(500.milliseconds)
                        googleMap.awaitSnapshot()?.let { bitmap ->
                            onSnapshot(bitmap)
                        }
                    }
                }
            }
        }

        if (!isRunFinished && currentLocation != null) {
            MarkerComposable(
                currentLocation,
                state = markerState
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RunIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }
    }

}
