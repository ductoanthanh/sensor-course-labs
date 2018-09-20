package com.example.mibeo.w2d1location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationCLient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private lateinit var currentLocationMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(R.layout.activity_main)

        askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        askForPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        askForPermission(android.Manifest.permission.INTERNET)
        askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        setupOsmMap()

        fusedLocationCLient = LocationServices.getFusedLocationProviderClient(this)

//        fusedLocationCLient.lastLocation.addOnSuccessListener {
//            location: Location? ->
//            Log.d("locationTest", "return result location")
//            if (location != null) {
//                tv_location.text = getString(R.string.result_location, location.longitude, location.latitude )
//            }
//        }
        createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.d("abc", "onLocationResult")
                locationResult ?: return
                val currentLocation = GeoPoint(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                Log.d("currentlocation",getString(R.string.result_location, locationResult.lastLocation.longitude, locationResult.lastLocation.latitude))
                showCurrentLocationOnMap(currentLocation)
            }
        }
    }

    private fun showCurrentLocationOnMap(location: GeoPoint) {
        mapView_osm.controller.setCenter(location)
        currentLocationMarker.position = location
        currentLocationMarker.icon = getDrawable(R.drawable.place)
        currentLocationMarker.title = "Latitude: ${location.latitude}\nLongitude: ${location.longitude}"

        if(mapView_osm.overlays.isNotEmpty()) {
            mapView_osm.overlays.clear()
        }
        mapView_osm.overlays.add(currentLocationMarker)

    }

    private fun setupOsmMap() {
        mapView_osm.setTileSource(TileSourceFactory.MAPNIK)

        mapView_osm.setBuiltInZoomControls(true)
        mapView_osm.setMultiTouchControls(true)

        mapView_osm.controller.setZoom(9.0)

        currentLocationMarker = Marker(mapView_osm)
        currentLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
    }

    private fun askForPermission(permission: String) {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = 5000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        Log.d("abc", "fun: startlocationUpdates")
        fusedLocationCLient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

}
