package com.rolekbartlomiej.weather_android.presentation.hello

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rolekbartlomiej.weather_android.R
import com.rolekbartlomiej.weather_android.databinding.FragmentHelloBinding
import com.rolekbartlomiej.weather_android.domain.service.data.all.AllWeather
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
        setOnClickListeners()
    }

    override fun onStart() {
        requestData()
        super.onStart()
    }

    private fun requestData() {
        viewModel.requestUserLocation(requireActivity())
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

        viewModel.showNoInternetInfo.collectIn(lifecycleScope) {
            binding.noInternetView.root.isVisible = it
        }
    }

    private fun setOnClickListeners() {
        binding.noInternetView.tryAgainBtn.setOnClickListener {
            requestData()
        }
    }

    private fun setViewData(weather: AllWeather) {
        with(binding) {
            val main = weather.current
            val uri = Uri.parse(Helpers.getImageUrl(main.weather[0].icon))
            Glide.with(requireActivity()).load(uri).into(weatherIcon)
            currentTempLbl.text = getString(R.string.temp_value, main.temp.toInt())
            currentWeatherDescLbl.text = main.weather[0].description
            windValueLbl.text = getString(R.string.wind_speed_value, main.wind_speed.toInt())
            humidityValueLbl.text = getString(R.string.humidity_value, main.humidity)
            pressureValueLbl.text = getString(R.string.pressure_value, main.pressure)
            cloudsLbl.text = getString(R.string.clouds_value, main.clouds)
            maxTempLbl.text = getString(R.string.temp_max_value, weather.daily[0].temp.max.toInt())
            minTempLbl.text = getString(R.string.temp_min_value, weather.daily[0].temp.min.toInt())
            feelsLikeWeatherLbl.text = getString(R.string.feels_like_temp, main.feels_like.toInt())
            sunriseValueLbl.text = DateHelper.toHour(main.sunrise)
            sunsetValueLbl.text = DateHelper.toHour(main.sunset)
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
        DialogHelper.showOkDialog(
            requireContext(), getString(R.string.location_permission),
            getString(R.string.location_permission_desc)
        )
    }

    companion object {
        private const val REQUEST_CODE_ASK_LOCATION_PERMS = 12323
    }
}