package com.rolekbartlomiej.weather_android.presentation.hello

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.rolekbartlomiej.weather_android.R
import com.rolekbartlomiej.weather_android.databinding.FragmentHelloBinding
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
//        binding.cityTxt.textChanges().debounce(500).collectIn(lifecycleScope) {
//            viewModel.searchCity(it.toString())
//        }

        observeViewModelValues()
    }

    override fun onStart() {
        viewModel.requestUserLocation(requireActivity())
        super.onStart()
    }

    private fun observeViewModelValues() {
        viewModel.weatherData.observe(viewLifecycleOwner) {
            binding.helloLbl.text = it.toString()
        }

        viewModel.permissionsToRequest.observe(viewLifecycleOwner) {
            requestPermissions(it.toTypedArray(), REQUEST_CODE_ASK_LOCATION_PERMS)
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