package com.rolekbartlomiej.weather_android.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    private val hourSdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun toHour(timestamp: Int): String = hourSdf.format(Date(timestamp.toLong() * 1000))
}