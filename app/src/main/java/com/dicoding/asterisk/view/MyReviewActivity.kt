package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityMyReviewBinding
import com.dicoding.asterisk.view.adapter.MyReviewAdapter
import com.dicoding.asterisk.view.adapter.RestaurantAdapter
import com.dicoding.asterisk.view.model.MainViewModel
import com.dicoding.asterisk.view.model.MainViewModelFactory
import com.dicoding.asterisk.view.model.MyReviewViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class MyReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyReviewBinding
    private lateinit var adapter: MyReviewAdapter

    private val viewModel: MyReviewViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk)
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = MyReviewAdapter { review, source ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_RESTAURANT_ID, review.id)
                putExtra(DetailActivity.EXTRA_IMAGE_URL, review.imageUrl)
                putExtra(DetailActivity.EXTRA_SOURCE, source)
                putExtra(DetailActivity.EXTRA_RESTAURANT_NAME, review.name)
                putExtra(DetailActivity.EXTRA_RESTAURANT_ADDRESS, review.address)
                putExtra(DetailActivity.EXTRA_RESTAURANT_REVIEW, review.review)
            }
            startActivity(intent)
        }
        binding.rvRestaurant.adapter = adapter
        binding.rvRestaurant.layoutManager = LinearLayoutManager(this)

        viewModel.reviews.observe(this) {
            adapter.setReviews(it)
        }

        viewModel.getSession().observe(this) { user ->
            user.username?.let {
                viewModel.fetchUserReviews(it)
            }
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
}