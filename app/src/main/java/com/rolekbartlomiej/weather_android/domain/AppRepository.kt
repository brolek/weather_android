package com.rolekbartlomiej.weather_android.domain

import com.rolekbartlomiej.weather_android.domain.service.AppServerService

class AppRepository(private val service: AppServerService) {

    suspend fun getWeatherByCityName(cityName: String) = service.getWeatherByCityName(cityName)

    suspend fun getWeatherByCoords(lat: Double, lon: Double) = service.getWeatherByCoords(lat, lon)

    suspend fun getAllWeatherByLocation(lat: Double, lon: Double) =
        service.getAllWeatherByLocation(lat, lon)
}