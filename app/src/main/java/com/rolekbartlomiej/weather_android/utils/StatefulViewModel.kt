package com.rolekbartlomiej.weather_android.utils

import androidx.lifecycle.ViewModel
import com.rolekbartlomiej.weather_android.domain.AppRepository
import com.rolekbartlomiej.weather_android.domain.service.AppServerService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
open class StatefulViewModel(private val repository: AppRepository) : ViewModel() {

    internal val isLoading = MutableStateFlow(true)
    internal val showNoInternetInfo = MutableStateFlow(false)

    fun onError(e: Error) {
        when (e) {
            is AppServerService.NoInternetError -> showNoInternetInfo.value = true
        }
        e.printStackTrace()
    }
}