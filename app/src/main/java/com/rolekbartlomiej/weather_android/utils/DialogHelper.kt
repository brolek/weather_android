package com.rolekbartlomiej.weather_android.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.rolekbartlomiej.weather_android.R

object DialogHelper {
    fun showOkDialog(context: Context, title: String, desc: String) = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(desc)
        .setPositiveButton(context.getString(R.string.ok)) { _, _ -> }
        .show()
}