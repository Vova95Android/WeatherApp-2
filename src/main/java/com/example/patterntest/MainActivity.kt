package com.example.patterntest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
     var viewModel=ViewModel()
     lateinit var navController: NavController
     var liveData= MutableLiveData<String>()
    lateinit var sity_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController=Navigation.findNavController(this,R.id.nav_host)
        viewModel.save_city=getSharedPreferences(
            viewModel.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
        if (viewModel.save_city!!.getString(Static().City_name.toString(), "0") != "0") {
            sity_name=
                viewModel.save_city!!.getString(Static().City_name.toString(), "0").toString()
        }
        else sity_name="0"

        val temp: String? = viewModel.save_city!!.getString(Static().LatLng.toString(), "0")
        var lat="0"
        var lng="0"
        if (!(viewModel.save_city!!.getString(Static().LatLng.toString(), "0") == "0")) {
            if (temp != null) {
                lat = temp.substring(temp.indexOf("(") + 1, temp.indexOf(","))
                lng = temp.substring(temp.indexOf(",") + 1, temp.indexOf(")"))
            }
        }

        viewModel.init(getString(R.string.api_wheather_id),lat,lng,getString(R.string.baseURL))
        viewModel.liveDataWeather.observe(this, Observer {
            viewModel.getActualWeather(it,sity_name,applicationContext)
            viewModel.getHourWeather(it,applicationContext,0)
            viewModel.getDayWeather(it,applicationContext,0)
        })
        viewModel.liveDataLatLng.observe(this, Observer {
            if (it.length>5) {
                val editor: SharedPreferences.Editor = viewModel.save_city!!.edit()
                editor.putString(Static().LatLng, it)
                editor.apply()
                lat = it.substring(it.indexOf("(") + 1, it.indexOf(","))
                lng = it.substring(it.indexOf(",") + 1, it.indexOf(")"))
                //viewModel.init(getString(R.string.api_wheather_id), lat, lng, getString(R.string.baseURL))
                val i=Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
            }
        })
        viewModel.liveDataCity.observe(this, Observer {
            if (it!="0") {
                val editor: SharedPreferences.Editor = viewModel.save_city!!.edit()
                editor.putString(Static().City_name, it)
                editor.apply()
            }
        })

    }
}
