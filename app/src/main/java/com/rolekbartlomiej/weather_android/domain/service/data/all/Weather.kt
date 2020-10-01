package com.rolekbartlomiej.weather_android.domain.service.data.all

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)