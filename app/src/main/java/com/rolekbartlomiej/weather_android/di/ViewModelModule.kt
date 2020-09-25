package com.rolekbartlomiej.weather_android.di

import com.rolekbartlomiej.weather_android.presentation.hello.HelloViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HelloViewModel(get()) }
}