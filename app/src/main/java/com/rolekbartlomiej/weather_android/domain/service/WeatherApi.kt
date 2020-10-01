package com.rolekbartlomiej.weather_android.domain.service

import com.rolekbartlomiej.weather_android.domain.service.data.current.ActualWeather
import com.rolekbartlomiej.weather_android.domain.service.data.all.AllWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") appId: String
    ): ActualWeather

    @GET("weather")
    suspend fun getWeatherByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): ActualWeather

    @GET("onecall")
    suspend fun getAllWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): AllWeather
}