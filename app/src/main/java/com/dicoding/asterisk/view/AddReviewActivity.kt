package com.dicoding.asterisk.view

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityAddReviewBinding
import com.dicoding.asterisk.view.model.AddReviewViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory
import com.bumptech.glide.Glide
import com.dicoding.asterisk.data.local.UserDataStore
import com.dicoding.asterisk.data.local.dataStore

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var userDataStore: UserDataStore
    private val viewModel by viewModels<AddReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk)
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataStore = UserDataStore.getInstance(this.dataStore)

        val restaurantName = intent.getStringExtra(EXTRA_RESTAURANT_NAME)
        val restaurantAddress = intent.getStringExtra(EXTRA_RESTAURANT_ADDRESS)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)

        setupUI(restaurantName, restaurantAddress, imageUrl)
        setupListeners()
        observeViewModel()
    }

    private fun setupUI(restaurantName: String?, restaurantAddress: String?, imageUrl: String?) {
        binding.tvNameRestaurant.text = restaurantName ?: "Unknown Restaurant"
        binding.tvAddressRestaurant.text = restaurantAddress ?: "Unknown Address"
        Glide.with(this).load(imageUrl).into(binding.ivRestaurantPhoto)
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.reviewResponse.observe(this) { response ->
            if (response != null) {
                Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Failed to submit review.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.showLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setupListeners() {
        binding.btnSubmitReview.setOnClickListener {
            val reviewText = binding.etReview.text.toString()
            val restaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
            val restaurantName = intent.getStringExtra(EXTRA_RESTAURANT_NAME)
            val restaurantImage = intent.getStringExtra(EXTRA_IMAGE_URL)
            val restaurantAdress = intent.getStringExtra(EXTRA_RESTAURANT_ADDRESS)

            viewModel.getSession().observe(this) { user ->
                val username = user.username
                if (reviewText.isNotEmpty() && restaurantId != null && restaurantName != null && restaurantImage != null && username != null && restaurantAdress != null) {
                    viewModel.submitReview(
                        reviewText,
                        restaurantId,
                        restaurantName,
                        restaurantImage,
                        username,
                        restaurantAdress

                    )
                    Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        R.string.empty_input,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_RESTAURANT_NAME = "extra_restaurant_name"
        const val EXTRA_RESTAURANT_ADDRESS = "extra_restaurant_address"
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_RESTAURANT_ID = "extra_restaurant_id"
    }
}