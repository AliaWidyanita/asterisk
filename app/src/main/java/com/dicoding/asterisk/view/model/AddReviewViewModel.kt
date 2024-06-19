package com.dicoding.asterisk.view.model

import androidx.lifecycle.*
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel(
    private val apiService: ApiService,
    private val repository: UserRepository,
): ViewModel() {

    private val _reviewResponse = MutableLiveData<ReviewResponse>()
    val reviewResponse: LiveData<ReviewResponse> = _reviewResponse

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun submitReview(reviewText: String, restaurantId : String, restaurantName : String, restaurantImage : String, username : String, address : String) {
        _showLoading.value = true
        apiService.submitReview(reviewText, restaurantId, restaurantName, restaurantImage, username, address).enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                _showLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _reviewResponse.value = response.body()
                } else {
                    _reviewResponse.value = null
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                _showLoading.value = false
                _reviewResponse.value = null
            }
        })
    }
}