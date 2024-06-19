package com.dicoding.asterisk.utils

import android.content.Context
import com.dicoding.asterisk.data.local.*
import com.dicoding.asterisk.data.remote.ApiConfig
import com.dicoding.asterisk.data.remote.ApiService

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val datastore = UserDataStore.getInstance(context.dataStore)
        val apiConfig = ApiConfig
        return UserRepository.getInstance(datastore, apiConfig)
    }

    fun provideApiService(): ApiService {
        return ApiConfig.getApiService("")
    }
}