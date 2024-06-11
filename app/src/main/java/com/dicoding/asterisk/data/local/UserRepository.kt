package com.dicoding.asterisk.data.local

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.asterisk.data.remote.ApiConfig
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.utils.RestaurantPagingSource
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val userDataStore: UserDataStore, private val apiConfig: ApiConfig) {
    suspend fun saveSession(user: User) {
        userDataStore.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userDataStore.getSession()
    }

    suspend fun logout() {
        userDataStore.logout()
    }

    fun getRestaurant(): LiveData<PagingData<RestaurantItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                RestaurantPagingSource(apiConfig ,userDataStore)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userDataStore: UserDataStore, apiConfig: ApiConfig): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userDataStore, apiConfig)
        }.also { instance = it }
    }
}