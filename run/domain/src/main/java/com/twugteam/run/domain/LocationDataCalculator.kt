package com.twugteam.run.domain

import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import kotlin.math.roundToInt

object LocationDataCalculator {
    fun getTotalDistanceMeter(locations: List<List<LocationTimestampWithAltitude>>): Int {
        return locations
            .sumOf { locationsTimestampsPerLine ->
                locationsTimestampsPerLine.zipWithNext { location1, location2 ->
                    location1.locationWithAltitude.location.distanceTo(location2.locationWithAltitude.location)
                }.sum().roundToInt()
            }
    }


}