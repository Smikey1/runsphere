package com.twugteam.core.domain.location

import kotlin.time.Duration

data class LocationTimestampWithAltitude(
    val locationWithAltitude: LocationWithAltitude,
    val durationTimestamp: Duration
)
