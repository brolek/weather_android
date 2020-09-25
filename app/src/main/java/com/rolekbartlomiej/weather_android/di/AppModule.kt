package com.rolekbartlomiej.weather_android.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rolekbartlomiej.weather_android.domain.AppRepository
import com.rolekbartlomiej.weather_android.domain.service.AppServerService
import com.rolekbartlomiej.weather_android.domain.service.WeatherApi
import com.rolekbartlomiej.weather_android.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    factory { createRetrofit(createOkHttpClient()) }
    factory { createNetworkApi(get()) }
    single { AppServerService(get()) }
    single { AppRepository(get()) }
}


fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor).build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .build()
}

fun createNetworkApi(retrofit: Retrofit): WeatherApi {
    return retrofit.create(WeatherApi::class.java)
}