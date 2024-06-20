package com.dicoding.asterisk.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityProfileBinding
import com.dicoding.asterisk.view.model.ProfileViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this@ProfileActivity) { user ->
            with(binding) {
                tvProfileName.text = user.fullName
                tvUserUsername.text = "@" + user.username
                tvUserFullName.text = user.fullName
                tvUserEmail.text = user.email
            }
        }

        binding.buttonLogin.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.valid_logout))
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.logout()
                    moveToWelcomeActivity()
                }
                setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }

        viewModel.showLoading.observe(this){
            showLoading(it)
        }

        setupBottomNavigation()
    }

    private fun moveToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
        binding.bottomNavigation.menu.findItem(R.id.action_profile).isChecked = true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}