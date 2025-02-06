package com.twugteam.core.domain.run

import com.twugteam.core.domain.location.Location
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    // Run id will be null if new run created
    val id: String?,
    val duration: Duration,
    val dateTimeUtc: ZonedDateTime,
    val distanceMeter: Int,
    val location: Location,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?
) {
    val avgSpeedKmh: Double
        get() = (distanceMeter / 1000.0) / duration.toDouble(DurationUnit.HOURS)
}

