package com.example.patterntest.weather_api

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object GetWeatherModel
{
    lateinit var id: String
    lateinit var lat: String
    lateinit var lng: String
    var retrofit: Retrofit? = null

    fun init(baseUrl: String){
        if (retrofit == null)
        retrofit =
            Retrofit.Builder() //Создаие билдера для GET запросов (Creating a builder for GET requests)
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
    }

    fun getWeather(data: MutableLiveData<String>){
        val messages: Call<String?>
        val getWeather = retrofit?.create(GetWeather::class.java)
        messages= getWeather?.weather(lat,lng,"alerts,minutely",id,"metric","ru")!!
        messages.enqueue(object: Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                data.value=response.body()
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {

            }
        })

    }

}

