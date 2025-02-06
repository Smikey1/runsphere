package com.twugteam.core.presentation.ui

import android.annotation.SuppressLint
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration


@SuppressLint("DefaultLocale")
fun Duration.toFormatted(): String {
    val totalSecond = inWholeSeconds
    val hours = String.format("%02d", totalSecond / 3600)
    val minutes = String.format("%02d", (totalSecond % 3600) / 60)
    val seconds = String.format("%02d", (totalSecond % 60))
    return "$hours:$minutes:$seconds"
}

fun Double.toFormattedKmh(): String {
    return "${this.roundToDecimalPlaces(1)} km/h"
}

fun Int.toFormattedMeters(): String {
    return "$this m"
}

fun Double.toFormattedKm(): String {
    return "${this.roundToDecimalPlaces(1)} km"
}

@SuppressLint("DefaultLocale")
fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }
    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val averagePaceMinute = secondsPerKm / 60
    val averagePaceSeconds = String.format("%02d", secondsPerKm % 60)
    return "$averagePaceMinute:$averagePaceSeconds / km"
}

private fun Double.roundToDecimalPlaces(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)
    return round(this * factor) / factor
}