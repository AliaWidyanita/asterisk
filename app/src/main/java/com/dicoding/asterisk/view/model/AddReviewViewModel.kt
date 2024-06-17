package com.dicoding.asterisk.view.model

import androidx.lifecycle.*
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.ReviewResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel(
    private val apiService: ApiService,
): ViewModel() {

    private val _reviewResponse = MutableLiveData<ReviewResponse>()
    val reviewResponse: LiveData<ReviewResponse> = _reviewResponse

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    fun submitReview(reviewText: String) {
        _showLoading.value = true
        apiService.submitReview(reviewText).enqueue(object : Callback<ReviewResponse> {
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
