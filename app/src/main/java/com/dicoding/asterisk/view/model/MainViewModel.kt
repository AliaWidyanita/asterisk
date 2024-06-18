package com.dicoding.asterisk.view.model

import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import android.Manifest
import android.content.pm.PackageManager
import com.dicoding.asterisk.data.local.User
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import com.dicoding.asterisk.data.remote.ReviewDetail

class MainViewModel(
    private val context: Context,
    private val repository: UserRepository,
    private val apiService: ApiService,
    private val fusedLocationClient: FusedLocationProviderClient,
) : ViewModel() {

    val listRestaurant: LiveData<PagingData<RestaurantItem>> =
        repository.getRestaurant().cachedIn(viewModelScope)
    val restaurantDetails = MutableLiveData<RestaurantItem>()
    val reviewStatistics = MutableLiveData<ReviewDetail>()


    private val _restaurants = MutableLiveData<List<RestaurantItem>>()
    val restaurants: LiveData<List<RestaurantItem>> = _restaurants

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun fetchNearbyRestaurants(latitude: Double, longitude: Double) {
        _showLoading.value = true
        apiService.getNearbyRestaurants(latitude, longitude, 10000)
            .enqueue(object : Callback<List<RestaurantItem>> {
                override fun onResponse(
                    call: Call<List<RestaurantItem>>,
                    response: Response<List<RestaurantItem>>
                ) {
                    _showLoading.value = false
                    if (response.isSuccessful) {
                        _restaurants.value = response.body()
                    } else {
                        _restaurants.value = emptyList()
                    }
                }

                override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                    _showLoading.value = false
                    _restaurants.value = emptyList()
                }
            })
    }

    fun searchRestaurants(query: String) {
        _showLoading.value = true
        apiService.searchRestaurants(query).enqueue(object : Callback<List<RestaurantItem>> {
            override fun onResponse(
                call: Call<List<RestaurantItem>>,
                response: Response<List<RestaurantItem>>
            ) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    _restaurants.value = response.body()
                } else {
                    _restaurants.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                _showLoading.value = false
                _restaurants.value = emptyList()
            }
        })
    }

    fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                fetchNearbyRestaurants(latitude, longitude)
            }
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
