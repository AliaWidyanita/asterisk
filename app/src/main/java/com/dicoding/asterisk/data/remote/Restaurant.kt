package com.dicoding.asterisk.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Restaurant (@field:SerializedName("restaurants")
    val articles: List<RestaurantItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)
@Parcelize
data class RestaurantItem(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("urlToImage")
    val urlToImage: String? = null
) : Parcelable

