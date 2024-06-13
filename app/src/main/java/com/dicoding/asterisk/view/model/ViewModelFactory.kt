package com.dicoding.asterisk.view.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.remote.ApiService
import com.dicoding.asterisk.utils.Injection
import com.google.android.gms.location.FusedLocationProviderClient

class ViewModelFactory(private val repository: UserRepository, private val context: Context,private val apiService: ApiService,
                       private val fusedLocationClient: FusedLocationProviderClient) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(context, repository, apiService, fusedLocationClient) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)-> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddReviewViewModel::class.java)-> {
                AddReviewViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, apiService: ApiService, fusedLocationClient: FusedLocationProviderClient): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context), context, apiService, fusedLocationClient)
            }.also { INSTANCE = it }
        }
    }
}