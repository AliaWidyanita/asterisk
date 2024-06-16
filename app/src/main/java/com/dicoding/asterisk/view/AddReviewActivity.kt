package com.dicoding.asterisk.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityAddReviewBinding
import com.dicoding.asterisk.view.model.AddReviewViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class AddReviewActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESTAURANT_NAME = "extra_restaurant_name"
        const val EXTRA_RESTAURANT_ADDRESS = "extra_restaurant_address"
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }

    private var currentImageUri: Uri? = null
    private lateinit var token: String
    private lateinit var binding: ActivityAddReviewBinding
    private val viewModel by viewModels<AddReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk);
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back()

        val restaurantName = intent.getStringExtra(EXTRA_RESTAURANT_NAME)
        val restaurantAddress = intent.getStringExtra(EXTRA_RESTAURANT_ADDRESS)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        binding.tvNameRestaurant.text = restaurantName ?: "Unknown Restaurant"
        binding.tvAddressRestaurant.text = restaurantAddress ?: "Unknown Address"
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivRestaurantPhoto)

        setupBottomNavigation()
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun moveToMyReviewActivity() {
        startActivity(Intent(this, MyReviewActivity::class.java))
    }

    private fun moveToProfileActivity() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_home_24_blue)
                    moveToMainActivity()
                    true
                }

                R.id.action_review -> {
                    item.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_review_24_blue)
                    moveToMyReviewActivity()
                    true
                }

                R.id.action_profile -> {
                    item.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_account_24_blue)
                    moveToProfileActivity()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    private fun back() {
        binding.ivBack.setOnClickListener { finish() }
    }
}