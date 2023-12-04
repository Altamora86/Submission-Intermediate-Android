package com.tugas.submissionawal.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.submissionawal.R
import com.tugas.submissionawal.adapter.LoadingStateAdapter
import com.tugas.submissionawal.databinding.ActivityMainBinding
import com.tugas.submissionawal.view.login.LoginRoom
import com.tugas.submissionawal.data.pref.UserPreference
import com.tugas.submissionawal.upload.UploadMenu
import com.tugas.submissionawal.adapter.StoryListAdapter
import com.tugas.submissionawal.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        adapter = StoryListAdapter()

        mainViewModel = ViewModelProvider(this,
            MainViewModel.ViewModelFactory(
                this,
                UserPreference.getInstance(dataStore)
            )
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.token.isNotEmpty()) {
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStoryPage(user.token).observe(this){
                    adapter.submitData(lifecycle, it)
                }
                Log.d("result main :", user.token)
            } else {
                startActivity(Intent(this, LoginRoom::class.java))
                finish()
            }
        }

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_maps -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                    Toast.makeText(this,"Maps", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.upload_menu -> {
                Intent(this, UploadMenu::class.java).also {
                    startActivity(it)
                    Toast.makeText(this, "Add Story", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.setting_menu-> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.btnLogout -> {
                Intent(this, LoginRoom::class.java).also {
                    startActivity(it)
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    mainViewModel.logout()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}