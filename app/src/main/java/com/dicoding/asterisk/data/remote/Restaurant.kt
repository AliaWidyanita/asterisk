package com.dicoding.asterisk.data.remote

import com.google.gson.annotations.SerializedName

data class Restaurant (@field:SerializedName("restaurants")
    val articles: List<RestaurantItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class RestaurantItem(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("rating")
    val rating: Integer? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("urlToImage")
    val urlToImage: String? = null
)