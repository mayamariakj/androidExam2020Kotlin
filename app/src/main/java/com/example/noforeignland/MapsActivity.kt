package com.example.noforeignland

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val featureName = intent.getStringExtra("featureName")
        val featureLat = intent.getDoubleExtra("featureLat", 0.0)
        val featureLon = intent.getDoubleExtra("featureLon", 0.0)

        val zoomLevel = 10.0f
        val feature = LatLng(featureLat, featureLon)
        mMap.addMarker(MarkerOptions().position(feature).title(featureName)).showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(feature, zoomLevel))
    }
}
