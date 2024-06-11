package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityMainBinding
import com.dicoding.asterisk.view.adapter.LoadingStateAdapter
import com.dicoding.asterisk.view.adapter.RestaurantAdapter
import com.dicoding.asterisk.view.model.MainViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLoggedIn) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setStoriesData()
            }
        }
        viewModel.showLoading.observe(this) {
            showLoading(it)
        }
        binding.fabAdd.setOnClickListener {
            moveToAddReviewActivity()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvRestaurant.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvRestaurant.addItemDecoration(itemDecoration)
    }

    private fun setStoriesData() {
        val adapter = RestaurantAdapter()
        binding.rvRestaurant.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.listRestaurant.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun moveToAddReviewActivity() {
        startActivity(Intent(this, AddReviewActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.valid_logout))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.logout()
                    }
                    setNegativeButton(getString(R.string.no)) { _, _ ->
                        return@setNegativeButton
                    }
                    create()
                    show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}