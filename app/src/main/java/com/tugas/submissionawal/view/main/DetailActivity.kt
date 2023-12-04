package com.tugas.submissionawal.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tugas.submissionawal.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra(NAME)
        val description = intent.getStringExtra(DESCRIPTION)
        val photo = intent.getStringExtra(URL)

        binding.description.text = description
        binding.userName.text = name
        Glide.with(this@DetailActivity)
            .load(photo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(binding.imageThumbnail)
    }
    companion object {
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val URL = "url"
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