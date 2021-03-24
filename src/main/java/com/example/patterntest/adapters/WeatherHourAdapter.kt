package com.example.patterntest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patterntest.R
import com.example.patterntest.data.WeatherHourData
import kotlinx.android.synthetic.main.weather_hour_ithem.view.*

class WeatherHourAdapter(
    context: Context?,
    resource: Int,
    weather_list: List<WeatherHourData>
):

    RecyclerView.Adapter<WeatherHourAdapter.MyViewHolder>() {
        private var inflater: LayoutInflater = LayoutInflater.from(context)
        private var layout = resource
        private var weathers=weather_list

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hourView: TextView?=null
        var cloudView: ImageView?=null
        var tempView: TextView?=null

        init {
            hourView = itemView.findViewById(R.id.textView) as TextView
            cloudView = itemView.findViewById(R.id.imageView) as ImageView
            tempView = itemView.findViewById(R.id.textView2) as TextView
        }
    }

        override fun getItemId(position: Int): Long {
            return weathers[position].hour.toLong()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount()=weathers.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val weather = weathers[position]
        holder.hourView?.text =weather.hour.toString()
        holder.cloudView?.setImageDrawable(weather.cloud)
        holder.tempView?.text =weather.temp.toString()+"°"
    }
}