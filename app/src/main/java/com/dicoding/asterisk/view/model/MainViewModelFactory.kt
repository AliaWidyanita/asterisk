package com.dicoding.asterisk.view.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.utils.Injection
import com.google.android.gms.location.FusedLocationProviderClient

class MainViewModelFactory(
    private val context: Context,
    private val repository: UserRepository,
    private val apiService: ApiService,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(context, repository, apiService, fusedLocationClient) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context, apiService: ApiService, fusedLocationClient: FusedLocationProviderClient): MainViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MainViewModelFactory(
                    context,
                    Injection.provideRepository(context),
                    apiService,
                    fusedLocationClient
                ).also { INSTANCE = it }
            }
        }
    }
}