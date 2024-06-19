package com.dicoding.asterisk.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.asterisk.R
import com.dicoding.asterisk.data.local.UserDataStore
import com.dicoding.asterisk.data.local.dataStore
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.data.remote.RestaurantReview
import com.dicoding.asterisk.data.remote.RestaurantStatisticsResponse
import com.dicoding.asterisk.databinding.ActivityDetailBinding
import com.dicoding.asterisk.view.model.DetailViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    private lateinit var userDataStore: UserDataStore

    private val reviewActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val restaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
            restaurantId?.let {
                viewModel.fetchStatistics(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDataStore = UserDataStore.getInstance(this.dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        val restaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
        val source = intent.getStringExtra(EXTRA_SOURCE)
        if (restaurantId != null) {
            if (source == "myReview") {
                viewModel.fetchRestaurantDetails(restaurantId)
                setDetails()
                binding.btnAddReview.setVisibility(View.GONE)
            } else {
                viewModel.fetchStatistics(restaurantId)
                observeStatistics()
            }
        }

        val detailRestaurant = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DETAIL, RestaurantItem::class.java)
        } else {
            intent.getParcelableExtra(KEY_DETAIL)
        }

        detailRestaurant?.let { restaurant ->
            binding.tvNameRestaurant.text = restaurant.name
            binding.tvAddressRestaurant.text = restaurant.address
            Glide.with(this).load(restaurant.imageUrl).into(binding.ivRestaurantPhoto)

        }
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(binding.ivRestaurantPhoto)
        }
        val restaurantName = intent.getStringExtra(EXTRA_RESTAURANT_NAME)
        if (restaurantName != null) {
            binding.tvNameRestaurant.text = restaurantName
        }

        val restaurantAddress = intent.getStringExtra(EXTRA_RESTAURANT_ADDRESS)
        if (restaurantAddress != null) {
            binding.tvAddressRestaurant.text = restaurantAddress
        }

        back()
        setupAddReviewButton()

        viewModel.showLoading.observe(this){
            showLoading(it)
        }
    }

    private fun observeStatistics() {
        viewModel.statistics.observe(this) { stats ->
            if (stats != null) {
                displayStatistics(stats)
            } else {
                displayDefaultMessage()
            }
        }
    }

    private fun setDetails() {
        val restaurantReview = intent.getStringExtra(EXTRA_RESTAURANT_REVIEW)
        if (restaurantReview != null) {
            binding.tvReviewResult1.text = restaurantReview
        }
    }

    private fun displayStatistics(stats: RestaurantStatisticsResponse) {
        val maxRating = 5.0
        val screenWidth = resources.displayMetrics.widthPixels
            binding.tvReviewResult1.text = "Food Average: ${stats.foodAvg}"
            binding.tvReviewResult2.text = "Ambience Average: ${stats.ambienceAvg}"
            binding.tvReviewResult3.text = "Service Average: ${stats.serviceAvg}"
            binding.tvReviewResult4.text = "Price Average: ${stats.priceAvg}"

            // Calculate and set the width for each progress bar
            binding.barFood.layoutParams.width = (stats.foodAvg.toFloat() / maxRating * screenWidth).toInt()
            binding.barAmbience.layoutParams.width = (stats.ambienceAvg.toFloat() / maxRating * screenWidth).toInt()
            binding.barService.layoutParams.width = (stats.serviceAvg.toFloat() / maxRating * screenWidth).toInt()
            binding.barPrice.layoutParams.width = (stats.priceAvg.toFloat() / maxRating * screenWidth).toInt()

            // Request layout to apply the changes
            binding.barFood.requestLayout()
            binding.barAmbience.requestLayout()
            binding.barService.requestLayout()
            binding.barPrice.requestLayout()
    }

    private fun displayDefaultMessage() {
        binding.tvReviewResult1.text = getString(R.string.message_review)
        binding.tvReviewResult2.visibility = View.GONE
        binding.tvReviewResult3.visibility = View.GONE
        binding.tvReviewResult4.visibility = View.GONE

        binding.barFood.visibility = View.GONE
        binding.barAmbience.visibility = View.GONE
        binding.barService.visibility = View.GONE
        binding.barPrice.visibility = View.GONE
    }

    private fun setupAddReviewButton() {
        val detailRestaurant = intent.getParcelableExtra<RestaurantItem>(KEY_DETAIL)
        detailRestaurant?.let { restaurant ->
            binding.btnAddReview.setOnClickListener {
                val intent = Intent(this, AddReviewActivity::class.java).apply {
                    putExtra(AddReviewActivity.EXTRA_RESTAURANT_NAME, restaurant.name)
                    putExtra(AddReviewActivity.EXTRA_RESTAURANT_ADDRESS, restaurant.address)
                    putExtra(AddReviewActivity.EXTRA_IMAGE_URL, restaurant.imageUrl)
                    putExtra(AddReviewActivity.EXTRA_RESTAURANT_ID, restaurant.restaurant_id)
                }
                reviewActivityLauncher.launch(intent)
            }
        }
    }

    private fun back() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_DETAIL = "key_detail"
        const val EXTRA_RESTAURANT_ID = "extra_restaurant_id"
        const val EXTRA_SOURCE = "extra_source"
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_RESTAURANT_NAME = "extra_restaurant_name"
        const val EXTRA_RESTAURANT_ADDRESS = "extra_restaurant_address"
        const val EXTRA_RESTAURANT_REVIEW = "extra_restaurant_review"
    }
}