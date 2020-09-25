package com.rolekbartlomiej.weather_android.presentation.hello

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.rolekbartlomiej.weather_android.domain.AppRepository
import com.rolekbartlomiej.weather_android.domain.service.data.ActualWeather
import com.rolekbartlomiej.weather_android.utils.EnableGpsLocationUtil
import com.rolekbartlomiej.weather_android.utils.isPermissionGranted
import kotlinx.coroutines.launch

class HelloViewModel(private val repository: AppRepository) : ViewModel() {
    internal val weatherData = MutableLiveData<ActualWeather>()
    internal val permissionsToRequest = MutableLiveData<List<String>>()

    internal fun searchCity(searchTxt: String) {
        viewModelScope.launch {
            repository.getWeatherByCityName(searchTxt)
        }
    }

    internal fun requestUserLocation(activity: Activity) {
        if (checkIfGpsEnabled(activity)) {
            getUserLastLocation(activity)
        } else {
            EnableGpsLocationUtil.requestEnableLocation(activity)
        }
    }

    private fun runIfPermissionsAreGranted(activity: Activity, block: () -> Unit) {
        val permissionsToRequest = ArrayList<String>().apply {
            WANTED_PERMISSIONS.forEach { addIfNotGranted(it, activity) }
        }

        if (permissionsToRequest.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.permissionsToRequest.postValue(permissionsToRequest)
            }
            return
        }
        block()
    }

    private fun ArrayList<String>.addIfNotGranted(permission: String, activity: Activity) {
        if (!activity.isPermissionGranted(permission)) {
            this.add(permission)
        }
    }

    private fun checkIfGpsEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getUserLastLocation(activity: Activity) {
        runIfPermissionsAreGranted(activity) {
            val client = LocationServices.getFusedLocationProviderClient(activity)
            client.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onLocationRetrieved(location)
                } else {
                    requestLastLocation(client, activity)
                }
            }
        }
    }

    private fun requestLastLocation(client: FusedLocationProviderClient, activity: Activity) {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)
            .setFastestInterval(1000)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        onLocationRetrieved(location)
                        client.removeLocationUpdates(this)
                    }
                }
            }
        }
        runIfPermissionsAreGranted(activity) {
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    private fun onLocationRetrieved(location: Location) {
        getWeatherForCoords(location)
    }

    private fun getWeatherForCoords(location: Location) {
        viewModelScope.launch {
            weatherData.value = repository.getWeatherByCoords(location.latitude, location.longitude)
        }
    }

    companion object {
        val WANTED_PERMISSIONS = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}