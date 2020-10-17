# Weatherly

Simple application showing the current weather for user location.

## Description

Weatherly uses data from [OpenWeatherAPI](https://openweathermap.org/api).
It shows such info as:
* current temperature with weather description and cloudiness,
* humidity, pressure and wind speed,
* sunrise and sunset hours,
* graph with the hourly temperature for the following 2 days.

## App Demo

<img src="demo.gif" width="300" />

## Tech stack

* Kotlin - no need for explanation,
* Koin - library for DI,
* Retrofit - HTTP client for getting data,
* Kotlin coroutines - used for implementing asynchronous code execution,
* Android Jetpack Navigation - navigating between fragments (only two fragments as yet :smile:),
* ViewModel - Jetpack's helper object while implementing MVVM app architecture,
* Lifecycle - handling lifecycle aware app components,
* View binding - an approach for accesing layout views widgets.

## Used libraries

* [MpAndroidChart](https://github.com/PhilJay/MPAndroidChart) - displaying hourly weather info.
