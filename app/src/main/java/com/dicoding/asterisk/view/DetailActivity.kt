package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.asterisk.R
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk);
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back()
        setupAddReviewButton()

        val detailRestaurant = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DETAIL, RestaurantItem::class.java)
        } else {
            intent.getParcelableExtra(KEY_DETAIL)
        }

        detailRestaurant?.let {
            binding.tvNameRestaurant.text = it.name
            binding.tvAddressRestaurant.text = it.address
            Glide.with(this)
                .load(it.imageUrl)
                .into(binding.ivRestaurantPhoto)
        }

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

    private fun setupAddReviewButton() {
        val detailRestaurant = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DETAIL, RestaurantItem::class.java)
        } else {
            intent.getParcelableExtra(KEY_DETAIL)
        }

        detailRestaurant?.let { restaurant ->
            binding.btnAddReview.setOnClickListener {
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra(AddReviewActivity.EXTRA_RESTAURANT_NAME, restaurant.name)
                intent.putExtra(AddReviewActivity.EXTRA_RESTAURANT_ADDRESS, restaurant.address)
                intent.putExtra(AddReviewActivity.EXTRA_IMAGE_URL, restaurant.imageUrl)
                startActivity(intent)
            }
        }
    }

    private fun back() {
        binding.ivBack.setOnClickListener { finish() }
    }

    companion object {
        const val KEY_DETAIL = "key_detail"
    }
}