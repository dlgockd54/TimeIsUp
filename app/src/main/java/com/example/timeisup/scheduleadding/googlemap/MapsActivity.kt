package com.example.timeisup.scheduleadding.googlemap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.timeisup.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG: String = MapsActivity::class.java.simpleName
    private val UPDATE_INTERVAL: Long = 1000 * 30
    private val FASTEST_UPDATE_INTERVAL: Long = 1000 * 10

    private lateinit var mMap: GoogleMap
    private var mCurrentMarker: Marker? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mCurrentLocation: Location
    private lateinit var mCurrentCoordinates: LatLng
    private val mLocationCallback: LocationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.d(TAG, "onLocationResult()")

            locationResult?.lastLocation?.let {
                Log.d(TAG, "last location: ${it.latitude}, longitude: ${it.longitude}")

                setCurrentLocation(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        Log.d(TAG, "onCreate()")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationSettingsRequestBuilder: LocationSettingsRequest.Builder
                = LocationSettingsRequest.Builder()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_UPDATE_INTERVAL
        }

        locationSettingsRequestBuilder.addLocationRequest(mLocationRequest)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady()")

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)

        mMap.uiSettings.isZoomControlsEnabled = true

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
            }
        }
    }

    private fun setCurrentLocation(location: Location) {
        mCurrentLocation = location
        mCurrentCoordinates = LatLng(mCurrentLocation.latitude, mCurrentLocation.longitude)

        setMarker()
    }

    private fun setMarker() {
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLng(mCurrentCoordinates)
        val markerOptions: MarkerOptions = MarkerOptions().apply {
            position(mCurrentCoordinates)
            title("현재 위치")
            draggable(true)
        }

        mCurrentMarker = mCurrentMarker ?: mMap.addMarker(markerOptions)
        mMap.moveCamera(cameraUpdate)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        overridePendingTransition(R.anim.animation_slide_from_left, R.anim.animation_slide_to_right)
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()

        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy()")

        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback) // Make the Google Play services disconnect
    }
}
