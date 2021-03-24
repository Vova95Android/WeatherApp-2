package com.example.patterntest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 10
    private var mMap: GoogleMap? = null
    private var Marker: Marker? = null
    private var position: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        //Получение актуальных координат(Getting the actual coordinates)
        val i = intent.extras
        position = i!!.getString(Static().LatLng, "(47.5016, 35.0818)")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val pos = LatLng(
            position!!.substring(position!!.indexOf("(") + 1, position!!.indexOf(",")).toDouble(),
            position!!.substring(position!!.indexOf(",") + 1, position!!.indexOf(")")).toDouble()
        )
        Marker = mMap!!.addMarker(
            MarkerOptions()
                .position(pos)
                .title("Укажите координаты")
                .draggable(true)
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap!!.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                position = marker.position.toString()
            }
        })
    }

    //Сохранить выбранные координаты(Save selected coordinates)
    fun saveLatLng(view: View?) {
        val i = Intent(this@MapsActivity, MainActivity::class.java)
        i.putExtra(Static().LatLng, position)
        i.putExtra(Static().City_name, "0")
        startActivity(i)
        finish()
    }

    fun onBack(view: View?) {
        val i = Intent(this@MapsActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // разрешение было предоставлено (permission was granted)
                fusedLocationClient!!.lastLocation
                    .addOnSuccessListener(
                        this
                    ) { location: Location? ->
                        if (location != null) {
                            val pos =
                                LatLng(location.latitude, location.longitude)
                            position =
                                "(" + location.latitude + "," + location.longitude + ")"
                            Marker!!.position = pos
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(pos))
                        }
                    }
            } else {
                // разрешение не было предоставлено (permission was not granted)
                Toast.makeText(this, "Please allow the use gps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Получение координат GPS(Getting GPS coordinates)
    fun getPos(view: View?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener(
                this
            ) { location: Location? ->
                if (location != null) {
                    val pos = LatLng(location.latitude, location.longitude)
                    position =
                        "(" + location.latitude + "," + location.longitude + ")"
                    Marker!!.position = pos
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(pos))
                }
            }
    }
}
