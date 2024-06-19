package com.dicoding.asterisk.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.RestaurantReview
import com.dicoding.asterisk.data.remote.RestaurantStatisticsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val apiService: ApiService) : ViewModel() {
    val statistics = MutableLiveData<RestaurantStatisticsResponse>()
    val reviewDetails = MutableLiveData<RestaurantReview>()

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    fun fetchStatistics(restaurantId: String) {
        _showLoading.value = true
        val authKey = mapOf("authenticationKey" to "c74706514d9bb65b3d51501846dbb08784e15d61aa274a54f03c8ab4af953776")
        apiService.getStatistics(authKey).enqueue(object : Callback<List<RestaurantStatisticsResponse>> {
            override fun onResponse(call: Call<List<RestaurantStatisticsResponse>>, response: Response<List<RestaurantStatisticsResponse>>) {
                _showLoading.value = false
                val stats = response.body()?.find { it.id == restaurantId }
                stats?.let {
                    statistics.postValue(it)
                }
            }

            override fun onFailure(call: Call<List<RestaurantStatisticsResponse>>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun fetchRestaurantDetails(restaurantId: String) {
        _showLoading.value = true
        apiService.getRestaurantDetails(restaurantId).enqueue(object : Callback<RestaurantStatisticsResponse> {
            override fun onResponse(call: Call<RestaurantStatisticsResponse>, response: Response<RestaurantStatisticsResponse>) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        statistics.postValue(it)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantStatisticsResponse>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "DetailViewModel"
    }
}