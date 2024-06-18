package com.dicoding.asterisk.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.asterisk.R
import com.dicoding.asterisk.data.local.UserDataStore
import com.dicoding.asterisk.data.local.dataStore
import com.dicoding.asterisk.data.remote.RestaurantItem
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
                viewModel.fetchStatistics(it) // Refresh the restaurant details
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk);
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDataStore = UserDataStore.getInstance(this.dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        val restaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
        if (restaurantId != null) {
            viewModel.fetchStatistics(restaurantId)
            observeStatistics()
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

        setupAddReviewButton()
        setupBottomNavigation()
        back()
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

    private fun displayStatistics(stats: RestaurantStatisticsResponse) {
        val statsText = "Food Average: ${stats.foodAvg}\n" +
                "Ambience Average: ${stats.ambienceAvg}\n" +
                "Service Average: ${stats.serviceAvg}\n" +
                "Price Average: ${stats.priceAvg}"
        binding.tvReviewResult.text = statsText
    }

    private fun displayDefaultMessage() {
        binding.tvReviewResult.text = getString(R.string.message_review)
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

    companion object {
        const val KEY_DETAIL = "key_detail"
        const val EXTRA_RESTAURANT_ID = "extra_restaurant_id"
    }
}