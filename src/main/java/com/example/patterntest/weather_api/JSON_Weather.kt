package com.example.weatherapp

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSON_Weather {
    //Открытые методы для получения переменных (Public methods for getting variables)
    //Переменные для считывания из JSON (Variables to read from JSON)
    // Актуальная погода (Actual weather)
    var temp = 0
        private set
    var temp_feels = 0
        private set
    var pressure = 0
        private set
    var humidity = 0
        private set
    var clouds = 0
        private set
    var wind_speed = 0
        private set
    var wind_deg = 0
        private set
    val description: String? = null
    var id = 0
        private set

    //Прогноз погоды(Weather forecast)
    var temp_day = 0
        private set
    var temp_morn = 0
        private set
    var temp_eve = 0
        private set
    var temp_night = 0
        private set
    var temp_feels_day = 0
        private set
    var temp_feels_morn = 0
        private set
    var temp_feels_eve = 0
        private set
    var temp_feels_night = 0
        private set
    var temp_min = 0
        private set
    var temp_max = 0
        private set

    //Получение JSON актуальной погоды(Getting JSON of the current weather)
    @Throws(JSONException::class)
    fun getActualWeather(jsonObject: JSONObject): JSONObject {
        return jsonObject.getJSONObject("current")
    }

    //Получение JSON погоды по часам(Getting weather JSON by hour)
    @Throws(JSONException::class)
    fun getHourlyWeather(jsonObject: JSONObject): JSONArray {
        return jsonObject.getJSONArray("hourly")
    }

    //Получение JSON погоды по дням(Getting JSON weather by day)
    @Throws(JSONException::class)
    fun getDaysWeather(jsonObject: JSONObject): JSONArray {
        return jsonObject.getJSONArray("daily")
    }

    //Получение объекта актуальной погоды или по часам(Receiving an object of the current weather or by the hour)
    fun getWeatherThisDayFromJson(jsonObject: JSONObject): JSON_Weather? {
        val p = JSON_Weather()
        try {
            p.temp = jsonObject.getInt("temp")
            p.temp_feels = jsonObject.getInt("feels_like")
            p.pressure = jsonObject.getInt("pressure")
            p.humidity = jsonObject.getInt("humidity")
            p.clouds = jsonObject.getInt("clouds")
            p.wind_speed = jsonObject.getInt("wind_speed")
            p.wind_deg = jsonObject.getInt("wind_deg")
            p.id = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id")
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
        return p
    }

    //Получение объекта погоды по дням(Getting a weather object by day)
    fun getWeatherNextDaysFromJson(jsonObject: JSONObject): JSON_Weather? {
        val p = JSON_Weather()
        try {
            p.temp_day = jsonObject.getJSONObject("temp").getInt("day")
            p.temp_morn = jsonObject.getJSONObject("temp").getInt("morn")
            p.temp_min = jsonObject.getJSONObject("temp").getInt("min")
            p.temp_max = jsonObject.getJSONObject("temp").getInt("max")
            p.temp_eve = jsonObject.getJSONObject("temp").getInt("eve")
            p.temp_night = jsonObject.getJSONObject("temp").getInt("night")
            p.temp_feels_day = jsonObject.getJSONObject("feels_like").getInt("day")
            p.temp_feels_morn = jsonObject.getJSONObject("feels_like").getInt("morn")
            p.temp_feels_eve = jsonObject.getJSONObject("feels_like").getInt("eve")
            p.temp_feels_night = jsonObject.getJSONObject("feels_like").getInt("night")
            p.pressure = jsonObject.getInt("pressure")
            p.humidity = jsonObject.getInt("humidity")
            p.clouds = jsonObject.getInt("clouds")
            p.wind_speed = jsonObject.getInt("wind_speed")
            p.wind_deg = jsonObject.getInt("wind_deg")
            p.id = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id")
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
        return p
    }
}