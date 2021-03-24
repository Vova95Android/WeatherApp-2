package com.example.patterntest.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.patterntest.MainActivity
import com.example.patterntest.MapsActivity
import com.example.patterntest.R
import com.example.patterntest.Static
import com.example.patterntest.adapters.WeatherDayAdapter
import com.example.patterntest.adapters.WeatherHourAdapter
import kotlinx.android.synthetic.main.fragment_weather.*

class Fragment_Weather : Fragment() {
    lateinit var data: String
    var pos=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // Inflate the layout for this fragment
        if ((activity as MainActivity).resources.configuration.orientation==Configuration.ORIENTATION_PORTRAIT)
        return inflater.inflate(R.layout.fragment_weather, container, false)
        else return inflater.inflate(R.layout.fragment_weather_horisontal, container, false)

        }

    override fun onStart() {
        super.onStart()
        image_place.setOnClickListener { (activity as MainActivity).navController.navigate(R.id.action_Fragment_Weather_to_Fragment_City) }
        image_location.setOnClickListener {
            val i= Intent ((activity as MainActivity),MapsActivity::class.java)
            i.putExtra(Static().LatLng, (activity as MainActivity).viewModel.save_city?.getString(Static().LatLng,"0),(0"))
            (activity as MainActivity).startActivity(i)
            (activity as MainActivity).finish()

        }
        textView_cityName.setOnClickListener { (activity as MainActivity).navController.navigate(R.id.action_Fragment_Weather_to_Fragment_City) }
        (activity as MainActivity).
        viewModel.liveDataActual.observe((activity as MainActivity), Observer {
                textView_actual_temp.text = it.temp.toString() + "°C"
                textView_actual_wind_speed.text = it.wind_speed.toString() + "м/сек"
                textView_actualHum.text = it.humidity.toString() + "%"
                imageView_main.setImageDrawable(it.cloud)
                imageView_actual_win_deg.setImageDrawable(it.wind_degree)
                textView_date.text = it.data
                textView_cityName.text = it.sity_name
        })
        (activity as MainActivity).viewModel.liveDataWeather.observe(this, Observer {data=it})
        (activity as MainActivity).viewModel.liveDataHourWeather.observe((activity as MainActivity),
            Observer {
                val hourWeather=it
                val adapter=WeatherHourAdapter((activity as MainActivity),R.layout.weather_hour_ithem,hourWeather)
                val l=LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL ,false)
                l.orientation=LinearLayoutManager.HORIZONTAL
                recHourWeather.layoutManager=l
                recHourWeather.adapter=adapter
            })
        (activity as MainActivity).viewModel.liveDataDayWeather.observe((activity as MainActivity),
            Observer {
                val dayWeather=it
                val adapter=WeatherDayAdapter((activity as MainActivity),R.layout.weather_day_ithem,dayWeather,pos)
                listDayWeather.adapter=adapter
                listDayWeather.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                    pos=position
                    (activity as MainActivity).viewModel.getHourWeather(data,(activity as MainActivity),position)
                    (activity as MainActivity).viewModel.getDayWeather(data,(activity as MainActivity),position)
                })
            })
    }

}
