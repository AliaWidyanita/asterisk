package com.dicoding.asterisk.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.data.local.UserRepository

class AddReviewViewModel(private val repository: UserRepository): ViewModel() {
//    private val _dataNewReview = MutableLiveData<AddReviewResponse?>()

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess : LiveData<Boolean> = _uploadSuccess

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}