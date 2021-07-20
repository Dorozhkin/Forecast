package com.example.forecast.util

import android.content.Context
import android.location.Geocoder
import android.location.Location
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class GeoUtil(private val geocoder: Geocoder, private val context: Context) {
    private val error = "error"

    suspend fun determineCityInitial(): String {
        return suspendCoroutine {
            val locationResult: LocationProvider.LocationResult = object : LocationProvider.LocationResult() {

                override fun gotLocation(location: Location?) {
                    val lat = location?.latitude
                    val lon = location?.longitude
                    try {
                        val address = geocoder.getFromLocation(lat!!, lon!!, 1)
                        it.resume(address[0].locality)
                    }
                    catch (e: java.lang.Exception) { }
                }
            }
            LocationProvider.getLocation(context, locationResult)
        }
    }

    fun determineCorrectName(cityName: String): String {

        return try {
            val geo = geocoder.getFromLocationName(cityName, 1)
            geo[0].locality
        }
        catch (e: Exception) {
            error
        }
    }
    fun determineLat(cityName: String): String {
        return try {
            val geo = geocoder.getFromLocationName(cityName, 1)
            geo[0].latitude.toString()
        }
        catch (e: Exception) {
            error
        }
    }
    fun determineLon(cityName: String): String {
        return try {
            val geo = geocoder.getFromLocationName(cityName, 1)
            geo[0].longitude.toString()
        }
        catch (e: Exception) {
            error
        }
    }
}