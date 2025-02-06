package com.twugteam.run.domain


import com.twugteam.core.domain.location.LocationTimestampWithAltitude
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
//    https://www.youtube.com/watch?v=UYNuxu3GvwU
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LocationTimestampWithAltitude>> = emptyList()

)
