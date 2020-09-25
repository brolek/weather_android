package com.rolekbartlomiej.weather_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setTheme(R.style.AppTheme)
//        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState, persistentState)

    }

}
