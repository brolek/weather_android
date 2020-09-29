package com.rolekbartlomiej.weather_android.presentation.hello

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rolekbartlomiej.weather_android.R
import com.rolekbartlomiej.weather_android.databinding.FragmentHelloBinding
import com.rolekbartlomiej.weather_android.domain.service.data.ActualWeather
import com.rolekbartlomiej.weather_android.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class HelloFragment : Fragment(R.layout.fragment_hello) {

    private val binding by viewBinding(FragmentHelloBinding::bind)
    private val viewModel: HelloViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModelValues()
    }

    override fun onStart() {
        viewModel.requestUserLocation(requireActivity())
        super.onStart()
    }

    private fun observeViewModelValues() {
        viewModel.weatherData.observe(viewLifecycleOwner) {
            setViewData(it)
        }

        viewModel.permissionsToRequest.observe(viewLifecycleOwner) {
            requestPermissions(it.toTypedArray(), REQUEST_CODE_ASK_LOCATION_PERMS)
        }

        viewModel.currentCityName.observe(viewLifecycleOwner) {
            binding.cityNameLbl.text = it
        }

        viewModel.isLoading.collectIn(lifecycleScope) {
            binding.loaderView.root.isVisible = it
        }
    }

    private fun setViewData(weather: ActualWeather) {
        with(binding) {
            val uri =
                Uri.parse("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
            Glide.with(requireActivity()).load(uri).into(weatherIcon)
            currentTempLbl.text = "${weather.main.temp.toInt()}°"
            currentWeatherDescLbl.text = weather.weather[0].description
            windValueLbl.text = "${weather.wind.speed.toInt()}m/s"
            humidityValueLbl.text = "${weather.main.humidity}%"
            pressureValueLbl.text = "${weather.main.pressure}hPa"
            cloudsLbl.text = "Zachmurzenie: ${weather.clouds.all}%"
            maxTempLbl.text = "${weather.main.temp_max.toInt()}°"
            minTempLbl.text = "${weather.main.temp_min.toInt()}°"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_ASK_LOCATION_PERMS -> for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    showLocationRequiredDialog()
                } else {
                    viewModel.requestUserLocation(requireActivity())
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == EnableGpsLocationUtil.REQUEST_ENABLE_GPS_LOCATION) {
            viewModel.requestUserLocation(requireActivity())
        }
    }

    private fun showLocationRequiredDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("lokalizacja")
            .setMessage("potrzeba lokalizacji")
            .setPositiveButton("ok") { _, _ -> }
            .show()
    }

    companion object {
        private const val REQUEST_CODE_ASK_LOCATION_PERMS = 12323
    }
}