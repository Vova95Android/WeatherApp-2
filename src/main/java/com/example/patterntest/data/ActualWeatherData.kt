package com.example.patterntest.data

import android.graphics.drawable.Drawable

class ActualWeatherData {
    lateinit var sity_name: String
    lateinit var data: String
    lateinit var cloud: Drawable
    var temp: Int = 0
    var humidity: Int = 0
    var wind_speed: Int = 0
    lateinit var wind_degree: Drawable
}