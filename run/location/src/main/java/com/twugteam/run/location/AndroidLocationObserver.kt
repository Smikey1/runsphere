package com.twugteam.run.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.twugteam.core.domain.location.LocationWithAltitude
import com.twugteam.run.domain.LocationObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.time.Duration.Companion.seconds

class AndroidLocationObserver(
    private val context: Context
) : LocationObserver {

    val clientFusedLocation = LocationServices.getFusedLocationProviderClient(context)
    override fun observeLocation(interval: Long): Flow<LocationWithAltitude> {
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGPSEnabled = false
            var isNetworkEnabled = false

            while (!isGPSEnabled && !isNetworkEnabled) {
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGPSEnabled && !isNetworkEnabled) {
                    delay(duration = 3.seconds)
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close() // this close the callback flow
            } else {
                clientFusedLocation.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        trySend(location.toLocationWithAltitude())
                    }
                }
                val request =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let {
                            trySend(it.toLocationWithAltitude())
                        }
                    }
                }
                clientFusedLocation.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
                // As soon as the flows close from Vm, then it will trigger await close block where we
                // remove the location updates
                awaitClose {
                    clientFusedLocation.removeLocationUpdates(locationCallback)
                }
            }
        }
    }

}