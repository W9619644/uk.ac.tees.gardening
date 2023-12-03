package com.example.greenthumb.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

class LocationUtils(private val context: Context) {

    private var locationManager: LocationManager? = null

    interface LocationListener {
        fun onLocationReceived(latitude: Double, longitude: Double)
    }

    fun requestLocationUpdates(listener: LocationListener) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted
            return
        }

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager



/*
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    location?.let {
                        listener.onLocationReceived(location.latitude, location.longitude)
                        locationManager?.removeUpdates(this)
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String?) {}

                override fun onProviderDisabled(provider: String?) {}
                override fun onLocationReceived(latitude: Double, longitude: Double) {
                    TODO("Not yet implemented")
                }
            }
        )*/
    }
}
