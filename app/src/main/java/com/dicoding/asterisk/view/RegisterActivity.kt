package com.dicoding.asterisk.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityRegisterBinding
import com.dicoding.asterisk.view.model.RegisterViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setupAction()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegisterPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(230)
        val tvUsername = ObjectAnimator.ofFloat(binding.tvRegisterUsername, View.ALPHA, 1f).setDuration(230)
        val etUsername = ObjectAnimator.ofFloat(binding.edRegisterUsername, View.ALPHA, 1f).setDuration(230)
        val tvFullName = ObjectAnimator.ofFloat(binding.tvRegisterFullName, View.ALPHA, 1f).setDuration(230)
        val etFullName = ObjectAnimator.ofFloat(binding.edRegisterFullName, View.ALPHA, 1f).setDuration(230)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvRegisterEmail, View.ALPHA, 1f).setDuration(230)
        val etEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(230)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvRegisterPassword, View.ALPHA, 1f).setDuration(230)
        val etPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(230)
        val signup = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(230)

        AnimatorSet().apply {
            playSequentially(title, tvUsername, etUsername, tvFullName, etFullName, tvEmail, etEmail, tvPassword, etPassword, signup)
            startDelay = 200
            start()
        }
    }

    private fun setupAction() {
        binding.buttonRegister.setOnClickListener {
            val username = binding.edRegisterUsername.text.toString()
            val fullName = binding.edRegisterFullName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            viewModel.getSession().observe(this@RegisterActivity) { user ->
                val token = user.token
                viewModel.postDataRegister(email, password, username, fullName, token)
            }
        }
        viewModel.showLoading.observe(this) {
            showLoading(it)
        }
        viewModel.registerSuccess.observe(this) { isSuccess->
            isRegisterSuccess(isSuccess)
        }
    }

    private fun isRegisterSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.register_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.failed))
                setMessage(getString(R.string.register_failed))
                setNegativeButton(getString(R.string.back)) { _, _ ->
                    return@setNegativeButton
                }
                create()
                show()
            }
        }
    }
}