@file:OptIn(InternalSerializationApi::class)

package com.twugteam.run.mappers

import com.twugteam.core.domain.location.Location
import com.twugteam.core.domain.run.Run
import com.twugteam.run.RunDto
import com.twugteam.run.network.CreateRunRequest
import kotlinx.serialization.InternalSerializationApi
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        duration = durationMillis.milliseconds,
        distanceMeter = distanceMeters,
        location = Location(lat, long),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toCreateRunRequestJson(): CreateRunRequest {
    return CreateRunRequest(
        id = id!!,
        epochMillis = dateTimeUtc.toEpochSecond() * 1000L,
        durationMillis = duration.inWholeMilliseconds,
        distanceMeters = distanceMeter,
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
    )
}