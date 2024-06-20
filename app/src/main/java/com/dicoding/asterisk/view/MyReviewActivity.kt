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
                    moveToMainActivity()
                    true
                }
                R.id.action_review -> {
                    moveToMyReviewActivity()
                    true
                }
                R.id.action_profile -> {
                    moveToProfileActivity()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.menu.findItem(R.id.action_review).isChecked = true
    }
}