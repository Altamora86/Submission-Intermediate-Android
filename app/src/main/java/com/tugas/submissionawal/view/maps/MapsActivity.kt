package com.tugas.submissionawal.view.maps

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tugas.submissionawal.R
import com.tugas.submissionawal.databinding.ActivityMapsBinding
import com.tugas.submissionawal.data.pref.UserPreference
import com.tugas.submissionawal.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Story Maps"

        mapsViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MapsViewModel::class.java]

        mapsViewModel.getUser().observe(this) { user ->
            Log.d("result main maps :", user.token)
            mapsViewModel.getMapsStory(user.token)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapsViewModel.getMaps().observe(this) {
            if (it.isNotEmpty()) {
                for (i in it) {
                    googleMap.addMarker(MarkerOptions().position(LatLng(i.lat,i.lon)).title(i.name).snippet(i.description))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(i.lat, i.lon)))
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}