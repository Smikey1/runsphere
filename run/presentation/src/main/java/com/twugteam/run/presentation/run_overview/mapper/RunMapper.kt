package com.twugteam.run.presentation.run_overview.mapper

import com.twugteam.core.domain.run.Run
import com.twugteam.core.presentation.ui.toFormatted
import com.twugteam.core.presentation.ui.toFormattedKm
import com.twugteam.core.presentation.ui.toFormattedKmh
import com.twugteam.core.presentation.ui.toFormattedMeters
import com.twugteam.core.presentation.ui.toFormattedPace
import com.twugteam.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {

    val dateTimeInLocalTimeZone = dateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTimeZone)

    val distanceKm = distanceMeter / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.toFormatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}