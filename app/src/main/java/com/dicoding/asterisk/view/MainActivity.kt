package com.dicoding.asterisk.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asterisk.R
import com.dicoding.asterisk.databinding.ActivityMainBinding
import com.dicoding.asterisk.view.adapter.RestaurantAdapter
import com.dicoding.asterisk.view.model.MainViewModel
import com.dicoding.asterisk.view.model.ViewModelFactory
import android.Manifest
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.dicoding.asterisk.data.remote.ApiConfig
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService
    private val _restaurants = MutableLiveData<List<RestaurantItem>>()

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        apiService = ApiConfig.getApiService()
//        setupRecyclerView()
//        checkLocationPermission()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLoggedIn) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                getToken { token ->
                    apiService = ApiConfig.getApiService(token)
                    setupRecyclerView()
                    checkLocationPermission()
                }
            }
        }
        viewModel.showLoading.observe(this) {
            showLoading(it)
        }
        binding.fabAdd.setOnClickListener {
            moveToAddReviewActivity()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantAdapter()
        binding.rvRestaurant.layoutManager = layoutManager
        binding.rvRestaurant.adapter = adapter
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvRestaurant.addItemDecoration(itemDecoration)

        _restaurants.observe(this) { restaurants ->
            adapter.submitList(restaurants)
        }
    }


    private fun moveToAddReviewActivity() {
        startActivity(Intent(this, AddReviewActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.valid_logout))
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.logout()
                    }
                    setNegativeButton(getString(R.string.no)) { _, _ ->
                        return@setNegativeButton
                    }
                    create()
                    show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            fetchLocation()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            fetchLocation()
        } else {
            // Permission denied
            Toast.makeText(this, "Location permission is needed to run this application", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                fetchNearbyRestaurants(latitude, longitude)
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error getting location", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchNearbyRestaurants(latitude: Double, longitude: Double) {
        showLoading(true)
        apiService.getNearbyRestaurants(latitude, longitude).enqueue(object : Callback<List<RestaurantItem>> {
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    _restaurants.value = response.body()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch restaurants", Toast.LENGTH_LONG).show()
                    _restaurants.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                _restaurants.value = emptyList()
            }
        })
    }

    private fun getToken(onTokenReceived: (String) -> Unit) {
        viewModel.getSession().observe(this) { user ->
            if (user.isLoggedIn) {
                onTokenReceived(user.token)
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}