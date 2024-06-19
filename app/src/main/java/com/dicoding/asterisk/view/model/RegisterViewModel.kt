package com.dicoding.asterisk.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiConfig
import com.dicoding.asterisk.data.remote.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun postDataRegister(email: String, password: String, userName: String, fullName: String, token: String) {
        _showLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService(token).register(email, password, userName, fullName)
            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    _showLoading.value = false
                    if (response.isSuccessful) {
                        val responseDetail = response.body()
                        if (responseDetail != null) {
                            _registerSuccess.value = true
                        }
                    } else {
                        _registerSuccess.value = false
                        _errorMessage.value = response.message()
                        Log.e(TAG, "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _showLoading.value = false
                    _registerSuccess.value = false
                    _errorMessage.value = "${t.message}"
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    companion object {
        const val TAG = "RegisterViewModel"
    }
}