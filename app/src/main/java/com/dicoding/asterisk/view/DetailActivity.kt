package com.dicoding.asterisk.view

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

        val detailRestaurant = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_DETAIL, RestaurantItem::class.java)
        } else {
            intent.getParcelableExtra(KEY_DETAIL)
        }

        if (detailRestaurant != null) {
//            binding.tvNameRestaurant.text = detailRestaurant.name.toString()
//            binding.tvAddressRestaurant.text = detailRestaurant.address.toString()
//            Glide.with(this@DetailActivity)
//                .load(detailRestaurant.photoUrl.toString())
//                .into(binding.ivPhotoRestaurant)
        }
        supportActionBar?.title = resources.getString(R.string.detail_restaurant, detailRestaurant?.name.toString())
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
}