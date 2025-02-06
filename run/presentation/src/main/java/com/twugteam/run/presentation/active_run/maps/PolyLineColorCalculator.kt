package com.twugteam.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import com.twugteam.core.presentation.designsystem.RunSphereDarkRed
import com.twugteam.core.presentation.designsystem.RunSphereGreen
import kotlin.math.abs

object PolyLineColorCalculator {

    fun locationsToColor(
        timestampLocation1: LocationTimestampWithAltitude,
        timestampLocation2: LocationTimestampWithAltitude
    ): Color {
        val distanceMeters =
            timestampLocation1.locationWithAltitude.location.distanceTo(timestampLocation2.locationWithAltitude.location)
        val timeDiff =
            abs((timestampLocation2.durationTimestamp - timestampLocation1.durationTimestamp).inWholeSeconds)
        val speedKmh = (distanceMeters / timeDiff) * 3.6
        
        return interpolateColor(
            speedKmh = speedKmh,
            minSpeed = 5.0,
            maxSpeed = 20.0,
            colorStart = RunSphereGreen,
            colorMid = Color.Yellow,
            colorEnd = RunSphereDarkRed
        )
    }

    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)
        val colorInt = if (ratio <= 0.5) {
            val midRatio = ((ratio - 0) / 0.5 - 0)
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = ((ratio - 0.5) / (1 - 0.5))
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }
        return Color(colorInt)
    }
}