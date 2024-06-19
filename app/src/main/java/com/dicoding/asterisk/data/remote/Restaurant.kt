package com.dicoding.asterisk.data.remote

import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName
import android.os.Parcelable

@Parcelize
data class RestaurantItem(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("restaurant_id")
    val restaurant_id: String? = null
) : Parcelable

data class ReviewResponse(
    val food: ReviewDetail,
    val ambience: ReviewDetail,
    val service: ReviewDetail,
    val price: ReviewDetail
)

data class ReviewDetail(
    val sentiment: String,
    val percentage: String
)

data class RestaurantStatisticsResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("foodAvg")
    val foodAvg: String,
    @SerializedName("ambienceAvg")
    val ambienceAvg: String,
    @SerializedName("serviceAvg")
    val serviceAvg: String,
    @SerializedName("priceAvg")
    val priceAvg: String
)

data class RestaurantReview(
    val review: String,
    val imageUrl: String,
    val name: String,
    val id: String,
    val address : String
)