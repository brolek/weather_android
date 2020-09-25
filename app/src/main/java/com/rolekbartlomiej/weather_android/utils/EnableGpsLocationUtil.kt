package com.rolekbartlomiej.weather_android.utils

import android.app.Activity
import android.content.IntentSender
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

object EnableGpsLocationUtil {
    fun requestEnableLocation(activity: Activity) {
        GoogleApiClient.Builder(activity).addApi(LocationServices.API).build().connect()

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val task =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                task.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvable = exception as ResolvableApiException
                            startIntentSenderForResult(
                                activity, exception.resolution.intentSender,
                                REQUEST_ENABLE_GPS_LOCATION, null, 0, 0, 0, null
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            e.printStackTrace()
                        }
                }
            }
        }
    }

    const val REQUEST_ENABLE_GPS_LOCATION = 10000
}