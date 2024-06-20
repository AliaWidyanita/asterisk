package com.dicoding.asterisk.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asterisk.R
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.databinding.ActivityLoginBinding
import com.dicoding.asterisk.view.model.LoginViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Asterisk)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        ObjectAnimator.ofFloat(binding.ivLoginPhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(230)
        val message = ObjectAnimator.ofFloat(binding.tvLoginMessage, View.ALPHA, 1f).setDuration(230)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvLoginEmail, View.ALPHA, 1f).setDuration(230)
        val etEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(230)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvLoginPassword, View.ALPHA, 1f).setDuration(230)
        val etPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(230)
        val login = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1f).setDuration(230)
        val layout = ObjectAnimator.ofFloat(binding.linearLayoutRegister, View.ALPHA, 1f).setDuration(230)

        AnimatorSet().apply {
            playSequentially(title, message, tvEmail, etEmail, tvPassword, etPassword, login, layout)
            startDelay = 200
            start()
        }
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.getSession().observe(this@LoginActivity) { user ->
                val token = user.token
                viewModel.postDataLogin(email, password, token)
            }
        }
        binding.tvClickRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        viewModel.showLoading.observe(this) {
            showLoading(it)
        }
        viewModel.loginSuccess.observe(this) { isSuccess->
            isLoginSuccess(isSuccess)
        }
    }

    private fun isLoginSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val username = viewModel.dataLogin.value?.username ?: ""
                    val fullName = viewModel.dataLogin.value?.fullName ?: ""
                    val email = viewModel.dataLogin.value?.email ?: ""
                    val token = viewModel.dataLogin.value?.token ?: ""
                    viewModel.saveSession(User(username, fullName, email, token, true))
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.failed))
                setMessage(getString(R.string.login_failed))
                setNegativeButton(getString(R.string.back)) { _, _ ->
                    return@setNegativeButton
                }
                create()
                show()
            }
        }
    }
}