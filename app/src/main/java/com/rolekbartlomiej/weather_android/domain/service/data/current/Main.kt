package com.rolekbartlomiej.weather_android.domain.service.data.current

data class Main(
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)