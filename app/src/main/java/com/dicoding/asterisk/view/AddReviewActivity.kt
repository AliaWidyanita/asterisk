package com.dicoding.asterisk.view

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.asterisk.databinding.ActivityAddReviewBinding
import com.dicoding.asterisk.view.model.AddReviewViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class AddReviewActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESTAURANT_NAME = "extra_restaurant_name"
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
    private var currentImageUri: Uri? = null
    private lateinit var token: String
    private lateinit var binding: ActivityAddReviewBinding
    private val viewModel by viewModels<AddReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantName = intent.getStringExtra(EXTRA_RESTAURANT_NAME)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        binding.tvRestaurantName.text = restaurantName ?: "Unknown Restaurant"

        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivRestaurant)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}