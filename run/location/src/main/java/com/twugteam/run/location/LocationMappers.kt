package com.twugteam.run.location

import android.location.Location
import com.twugteam.core.domain.location.LocationWithAltitude


fun Location.toLocationWithAltitude(): LocationWithAltitude {
    return LocationWithAltitude(
        location = com.twugteam.core.domain.location.Location(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}