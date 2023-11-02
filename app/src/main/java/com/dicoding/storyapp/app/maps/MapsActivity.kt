package com.dicoding.storyapp.app.maps

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.app.home.MainViewModel
import com.dicoding.storyapp.core.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true


        mMap.setMinZoomPreference(5.0f)
        mMap.setMaxZoomPreference(14.0f)

        val token = mainViewModel.getPreference(this)
        getStory(token.value.toString())
    }


    private fun getStory(token: String) {
        mapsViewModel.getStoriesWithLocation("Bearer $token").observe(this) {
            try {
                if (it != null) {
                    when (it) {
                        is com.dicoding.storyapp.core.data.Result.Success -> {
                            val storyList = it.data
                            addingMarkerToMaps(storyList)
//                            Log.d("Array Maps Sukses: ", storyList.toString())
                        }
                        is com.dicoding.storyapp.core.data.Result.Loading -> {
//                            Log.d("Maps Loading", "Loading")
                        }
                        is com.dicoding.storyapp.core.data.Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
//                            Log.d("Maps Gagal: ", it.error)
                        }
                    }
                }
            } catch (e: Exception) {
//                Log.d("ERROR MAPS ", e.message.toString())
            }
        }
    }
    private val boundsBuilder = LatLngBounds.Builder()
    private fun addingMarkerToMaps(story: List<ListStoryItem>) {
        story.map { data ->
            val latLng = LatLng(data.lat!!, data.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.name)
            )
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )

        val center = LatLng(-7.8257076,110.3904714)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))
    }
}