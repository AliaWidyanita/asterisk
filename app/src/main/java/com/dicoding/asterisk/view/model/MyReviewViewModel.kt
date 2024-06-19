package com.dicoding.asterisk.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.RestaurantReview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewViewModel(private val apiService: ApiService, private val repository: UserRepository) : ViewModel() {
    private val _reviews = MutableLiveData<List<RestaurantReview>>()
    val reviews: LiveData<List<RestaurantReview>> = _reviews

    fun fetchUserReviews(identifier: String) {
        apiService.getUserReviews(identifier).enqueue(object : Callback<List<RestaurantReview>> {
            override fun onResponse(call: Call<List<RestaurantReview>>, response: Response<List<RestaurantReview>>) {
                if (response.isSuccessful) {
                    _reviews.postValue(response.body())
                } else {
                    _reviews.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<RestaurantReview>>, t: Throwable) {
                _reviews.postValue(emptyList())
            }
        })
    }
    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}