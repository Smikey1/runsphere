package com.twugteam.run.network

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable


@InternalSerializationApi
@Serializable
data class CreateRunRequest(
    val durationMillis: Long,
    val distanceMeters: Int,
    val epochMillis: Long,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val id: String
)
