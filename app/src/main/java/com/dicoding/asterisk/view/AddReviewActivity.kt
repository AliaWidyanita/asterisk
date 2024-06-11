package com.dicoding.asterisk.view

import android.net.Uri
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asterisk.databinding.ActivityAddReviewBinding
import com.dicoding.asterisk.view.model.AddReviewViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory

class AddReviewActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var token: String
    private lateinit var binding: ActivityAddReviewBinding
    private val viewModel by viewModels<AddReviewViewModel> {
        ViewModelFactory.getInstance(this)
    }
}