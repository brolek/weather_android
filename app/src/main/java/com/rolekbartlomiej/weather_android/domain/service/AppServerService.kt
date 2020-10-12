package com.rolekbartlomiej.weather_android.domain.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.rolekbartlomiej.weather_android.domain.service.data.current.ActualWeather
import com.rolekbartlomiej.weather_android.domain.service.data.all.AllWeather
import com.rolekbartlomiej.weather_android.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AppServerService(private val context: Context, private val api: WeatherApi) {
    private val TAG = "INFO11"

    suspend fun getWeatherByCityName(cityName: String): ActualWeather {
        return withContext(Dispatchers.IO) {
            var weather: ActualWeather? = null
            try {
                weather = api.getWeatherByCityName(cityName, Constants.WEATHER_KEY)
            } catch (e: Exception) {
                Log.e(TAG, "getWeatherByCityName: " + e.message)
            }
            weather!!
        }
    }

    suspend fun getWeatherByCoords(lat: Double, long: Double): ActualWeather {
        return withContext(Dispatchers.IO) {
            var weather: ActualWeather? = null
            try {
                weather = api.getWeatherByCoords(lat, long, Constants.WEATHER_KEY)
            } catch (e: Exception) {
                Log.e(TAG, "getWeatherByCoords: " + e.message)
            }
            weather!!
        }
    }

    suspend fun getAllWeatherByLocation(lat: Double, long: Double): AllWeather? {
        return if (handleNoInternetConnection()) {
            withContext(Dispatchers.IO) {
                var weather: AllWeather? = null
                try {
                    weather = api.getAllWeatherByLocation(lat, long, Constants.WEATHER_KEY)
                } catch (e: Exception) {
                    Log.e(TAG, "getAllWeatherByLocation: " + e.message)
                }
                weather!!
            }
        } else {
            null
        }
    }

    private fun handleNoInternetConnection() =
        if (isNetworkConnected()) true else throw NoInternetError()

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    class NoInternetError : Error()
}

