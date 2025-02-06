package com.twugteam.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.twugteam.core.domain.location.LocationTimestampWithAltitude

@Composable
fun RunSpherePolyline(
    locations: List<List<LocationTimestampWithAltitude>>
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { locationTimestamp1, locationTimestamp2 ->
                PolylineUi(
                    location1 = locationTimestamp1.locationWithAltitude.location,
                    location2 = locationTimestamp2.locationWithAltitude.location,
                    color = PolyLineColorCalculator.locationsToColor(
                        locationTimestamp1,
                        locationTimestamp2
                    )
                )
            }
        }
    }

    polylines.forEach { polylineUiList ->
        polylineUiList.forEach { polylineUi ->
            Polyline(
                points = listOf<LatLng>(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.long),
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }

}