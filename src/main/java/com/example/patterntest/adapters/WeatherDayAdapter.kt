package com.example.patterntest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.patterntest.R
import com.example.patterntest.data.WeatherDayData

class WeatherDayAdapter (
    private val context: Context?,
    resource: Int,
    weather_list: List<WeatherDayData>,
    private val pos: Int
    ):

    BaseAdapter() {
        private var inflater: LayoutInflater = LayoutInflater.from(context)
        private var layout = resource
        private var weathers=weather_list

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view: View = inflater.inflate(layout, parent, false)
            val dayView = view.findViewById(R.id.textView3) as TextView
            val tempView = view.findViewById(R.id.textView4) as TextView
            val cloudView = view.findViewById(R.id.imageView2) as ImageView
            val weather = weathers[position]
            if (context != null) {
                if (position == pos) {
                    dayView.setTextColor(context.getColor(R.color.colorBackgraund2))
                    tempView.setTextColor(context.getColor(R.color.colorBackgraund2))
                } else{
                    dayView.setTextColor(context.getColor(R.color.colorTextBlack))
                    tempView.setTextColor(context.getColor(R.color.colorTextBlack))
                }
            }
            dayView.text=weather.day_week
            cloudView.setImageDrawable(weather.cloud)
            tempView.text=weather.max_min_temp
            return view
        }

        override fun getItem(position: Int): Any {
            return weathers[position]
        }

        override fun getItemId(position: Int): Long {
            return 1
        }

        override fun getCount(): Int {
            return weathers.size
        }
    }