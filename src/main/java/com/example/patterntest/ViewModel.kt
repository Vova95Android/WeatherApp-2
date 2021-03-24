package com.example.patterntest

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.example.patterntest.data.ActualWeatherData
import com.example.patterntest.data.WeatherDayData
import com.example.patterntest.data.WeatherHourData
import com.example.patterntest.weather_api.GetWeatherModel
import com.example.weatherapp.JSON_Weather
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class ViewModel
{
    private val day_week =
        arrayOf("ВС", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ")
    var liveDataActual= MutableLiveData<ActualWeatherData>()
    var liveDataHourWeather=MutableLiveData<MutableList<WeatherHourData>>()
    var liveDataDayWeather=MutableLiveData<MutableList<WeatherDayData>>()
    var liveDataWeather= MutableLiveData<String>()
    var liveDataCity= MutableLiveData<String>()
    var liveDataLatLng= MutableLiveData<String>()
    val APP_PREFERENCES = "save_sity"
    var save_city: SharedPreferences? = null

    fun init(id: String, lat:String, lng: String, baseURL:String){
        GetWeatherModel.id=id
        GetWeatherModel.lat=lat
        GetWeatherModel.lng=lng
        GetWeatherModel.init(baseURL)
        GetWeatherModel.getWeather(liveDataWeather)
    }

    fun getActualWeather(data: String, city:String, context: Context) {
        val formatter =
            DateTimeFormatter.ofPattern("HH")
        val time = LocalTime.now()
        val date = time.format(formatter)
        val hour = date.toInt()
        val JSONdata=JSONObject(data)
        val actualJSON=JSON_Weather().getActualWeather(JSONdata)
        val actual=ActualWeatherData()
        actual.temp = JSON_Weather().getWeatherThisDayFromJson(actualJSON)?.temp!!
        actual.humidity = JSON_Weather().getWeatherThisDayFromJson(actualJSON)?.humidity!!
        actual.wind_speed = JSON_Weather().getWeatherThisDayFromJson(actualJSON)?.wind_speed!!
        when(JSON_Weather().getWeatherThisDayFromJson(actualJSON)?.wind_deg){
            in 0..45->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_e)!!
            in 46..90->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_se)!!
            in 91..135->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_s)!!
            in 136..180->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_ws)!!
            in 181..225->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_w)!!
            in 226..270->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_wn)!!
            in 271..315->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_n)!!
            in 316..360->actual.wind_degree= context.getDrawable(R.drawable.ic_icon_wind_ne)!!
        }
        actual.cloud=getCloud(JSON_Weather().getWeatherThisDayFromJson(actualJSON)!!.id,hour,context)
        val c = Calendar.getInstance()
        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        val formatter1 =
            DateTimeFormatter.ofPattern("dd MMM")
        val time1 = LocalDate.now()
        val date1 = time1.format(formatter1)
        actual.data= day_week.get(dayOfWeek - 1).toString() + ", " + date1
        actual.sity_name=city
        liveDataActual.value=actual
    }

    fun getHourWeather(data: String,context: Context, index:Int){
        val formatter =
            DateTimeFormatter.ofPattern("HH")
        val time = LocalTime.now()
        val date = time.format(formatter)
        val hour = date.toInt()
        val JSONdata=JSONObject(data)
        val ListHourWeather= mutableListOf<WeatherHourData>()
        if (index==0){
            val JSONarrayHour=JSON_Weather().getHourlyWeather(JSONdata)
            for(i in hour..24) {
                   val json = JSON_Weather().getWeatherThisDayFromJson(JSONarrayHour.getJSONObject(i-hour))
                if (json != null) {
                    val w= WeatherHourData()
                    w.temp=json.temp
                    w.cloud=getCloud(json.id,hour,context)
                    w.hour=i
                    ListHourWeather.add(w)
                }
            }
        }
        else{
            val JSONarrayHour=JSON_Weather().getDaysWeather(JSONdata)
             val json = JSON_Weather().getWeatherNextDaysFromJson(JSONarrayHour.getJSONObject(index))
                if (json != null) {
                    var w= WeatherHourData()
                    w.temp=json.temp_night
                    w.cloud=getCloud(json.id,hour,context)
                    w.hour=2
                    ListHourWeather.add(w)
                    w= WeatherHourData()
                    w.temp=json.temp_morn
                    w.cloud=getCloud(json.id,hour,context)
                    w.hour=8
                    ListHourWeather.add(w)
                    w= WeatherHourData()
                    w.temp=json.temp_day
                    w.cloud=getCloud(json.id,hour,context)
                    w.hour=14
                    ListHourWeather.add(w)
                    w= WeatherHourData()
                    w.temp=json.temp_eve
                    w.cloud=getCloud(json.id,hour,context)
                    w.hour=20
                    ListHourWeather.add(w)
                }

        }

        liveDataHourWeather.value=ListHourWeather

    }

    fun getDayWeather(data: String,context: Context, num: Int){
        val days_json = JSON_Weather().getDaysWeather(JSONObject(data))
        val c = Calendar.getInstance()
        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        val ListDayWeather= mutableListOf<WeatherDayData>()
        for (count_days in 0..6){
            val json =
                JSON_Weather().getWeatherNextDaysFromJson(days_json.getJSONObject(count_days))
            if (json != null) {
                val w= WeatherDayData()
                if (((dayOfWeek-1) + count_days) < 7) w.day_week=day_week[(dayOfWeek-1) + count_days]
                else w.day_week=day_week[((dayOfWeek-1) + count_days)-7]
                w.max_min_temp= json.temp_max.toString() + "°/" + json.temp_min + "°"
                when(json.id) {
                    in 0..300-> {
                        if (num==count_days)  w.cloud= context.getDrawable(R.drawable.ic_blue_day_thunder)!!
                        else w.cloud= context.getDrawable(R.drawable.ic_black_day_thunder)!!
                    }
                    in 301..500->{
                        if (num==count_days) w.cloud= context.getDrawable(R.drawable.ic_blue_day_rain)!!
                        else w.cloud= context.getDrawable(R.drawable.ic_black_day_rain)!!
                    }
                    in 501..700->{
                        if (num==count_days) w.cloud= context.getDrawable(R.drawable.ic_blue_day_shower)!!
                        else w.cloud= context.getDrawable(R.drawable.ic_black_day_shower)!!
                    }
                    800->{
                        if (num==count_days) w.cloud= context.getDrawable(R.drawable.ic_blue_day_bright)!!
                        else w.cloud= context.getDrawable(R.drawable.ic_black_day_bright)!!
                    }
                    else->{
                        if (num==count_days) w.cloud= context.getDrawable(R.drawable.ic_blue_day_cloudy)!!
                        else w.cloud= context.getDrawable(R.drawable.ic_black_day_cloudy)!!
                    }
                }
                ListDayWeather.add(w)
            }
        }
        liveDataDayWeather.value=ListDayWeather
    }


    fun getCloud(cloud: Int, hour: Int, context: Context): Drawable {
        val draw: Drawable

        when(cloud) {
            in 0..300-> {
                if (hour < 18 && hour > 5)  draw= context.getDrawable(R.drawable.ic_white_day_thunder)!!
                else draw= context.getDrawable(R.drawable.ic_white_night_thunder)!!
            }
            in 301..500->{
                if (hour < 18 && hour > 5) draw= context.getDrawable(R.drawable.ic_white_day_rain)!!
                else draw= context.getDrawable(R.drawable.ic_white_night_rain)!!
            }
            in 501..700->{
                if (hour < 18 && hour > 5) draw= context.getDrawable(R.drawable.ic_white_day_shower)!!
                else draw= context.getDrawable(R.drawable.ic_white_night_shower)!!
            }
            800->{
                if (hour < 18 && hour > 5) draw= context.getDrawable(R.drawable.ic_white_day_bright)!!
                else draw= context.getDrawable(R.drawable.ic_white_night_bright)!!
            }
            else->{
                if (hour < 18 && hour > 5) draw= context.getDrawable(R.drawable.ic_white_day_cloudy)!!
                else draw= context.getDrawable(R.drawable.ic_white_night_cloudy)!!
            }
        }
        return draw
    }
}