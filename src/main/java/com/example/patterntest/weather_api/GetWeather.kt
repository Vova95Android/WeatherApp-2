package com.example.patterntest.weather_api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetWeather {

    @GET("onecall?.json")
    fun weather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("exclude") exclude: String?,
        @Query("appid") appid: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?
    ): Call<String?>?

}