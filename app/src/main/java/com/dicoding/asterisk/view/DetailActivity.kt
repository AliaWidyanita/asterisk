package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.asterisk.R
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                .into(binding.ivPhotoRestaurant)
            supportActionBar?.title = getString(R.string.detail_restaurant, it.name)
        }
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

    companion object {
        const val KEY_DETAIL = "key_detail"
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
                intent.putExtra(AddReviewActivity.EXTRA_IMAGE_URL, restaurant.imageUrl)
                startActivity(intent)
            }
        }
    }
}