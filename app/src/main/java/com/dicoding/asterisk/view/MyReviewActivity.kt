package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityMyReviewBinding
import com.dicoding.asterisk.view.adapter.MyReviewAdapter
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

        adapter = MyReviewAdapter()
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
                    updateNavIcon(item, R.drawable.ic_baseline_home_24_blue)
                    moveToMainActivity()
                }

                R.id.action_review -> {
                    updateNavIcon(item, R.drawable.ic_baseline_review_24_blue)
                    moveToMyReviewActivity()
                }

                R.id.action_profile -> {
                    updateNavIcon(item, R.drawable.ic_baseline_account_24_blue)
                    moveToProfileActivity()
                }
            }
            true
        }
    }

    private fun updateNavIcon(item: MenuItem, iconResId: Int) {
        item.icon = ContextCompat.getDrawable(this, iconResId)
        resetNavIconsExcept(item)
    }

    private fun resetNavIconsExcept(exceptItem: MenuItem) {
        val menu = binding.bottomNavigation.menu
        for (item in menu) {
            if (item!= exceptItem) {
                when (item.itemId) {
                    R.id.action_home -> item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_home_24)
                    R.id.action_review -> item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_review_24)
                    R.id.action_profile -> item.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_account_24)
                }
            }
        }
    }
}