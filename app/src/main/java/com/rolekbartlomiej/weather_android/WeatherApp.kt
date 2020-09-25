package com.rolekbartlomiej.weather_android

import android.app.Application
import com.rolekbartlomiej.weather_android.di.appModule
import com.rolekbartlomiej.weather_android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(appModule, viewModelModule))
        }
    }
}