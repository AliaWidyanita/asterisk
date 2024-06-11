package com.dicoding.asterisk.utils

import android.content.Context
import com.dicoding.asterisk.data.local.UserDataStore
import com.dicoding.asterisk.data.local.UserRepository
import com.dicoding.asterisk.data.local.dataStore
import com.dicoding.asterisk.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val datastore = UserDataStore.getInstance(context.dataStore)
        val apiConfig = ApiConfig
        return UserRepository.getInstance(datastore, apiConfig)
    }
}