package com.dicoding.asterisk.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.asterisk.data.local.UserDataStore
import com.dicoding.asterisk.data.remote.ApiConfig
import com.dicoding.asterisk.data.remote.RestaurantItem

class RestaurantPagingSource(private val apiConfig: ApiConfig, private val dataStore: UserDataStore) : PagingSource<Int, RestaurantItem>() {
    override fun getRefreshKey(state: PagingState<Int, RestaurantItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RestaurantItem> {
        TODO("Not yet implemented")
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}