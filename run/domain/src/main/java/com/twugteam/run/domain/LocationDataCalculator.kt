package com.twugteam.run.domain

import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {
    fun getTotalDistanceMeter(locations: List<List<LocationTimestampWithAltitude>>): Int {
        return locations
            .sumOf { locationsTimestampsPerLine ->
                locationsTimestampsPerLine.zipWithNext { location1, location2 ->
                    location1.locationWithAltitude.location.distanceTo(location2.locationWithAltitude.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationTimestampWithAltitude>>): Double {
        return location.maxOf { locationSet ->
            val speedKmhList = locationSet.zipWithNext { location1, location2 ->
                val distance = location1.locationWithAltitude.location.distanceTo(location2.locationWithAltitude.location)
                val distanceKm = distance / 1000.0
                val hoursDiff = (location2.durationTimestamp - location1.durationTimestamp).toDouble(DurationUnit.HOURS)

                if(hoursDiff == 0.0) {
                    0.0
                } else {
                    distanceKm/hoursDiff
                }
            }
            speedKmhList.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationMeters(locations: List<List<LocationTimestampWithAltitude>>): Int {
        return locations
            .sumOf { locationsSet ->
                locationsSet.zipWithNext { location1, location2 ->
                    val altitude1 = location1.locationWithAltitude.altitude
                    val altitude2 = location2.locationWithAltitude.altitude

                    // at least 0 if you gain altitude of 100 meter in climbing hills and
                    // again comes to bottom of hills, then no altitude gain. it cannot be minus, so at-least 0
                    (altitude2-altitude1).coerceAtLeast(0.0)
                }.sum().roundToInt()
            }
    }


}