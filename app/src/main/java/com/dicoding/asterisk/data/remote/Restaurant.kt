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

data class Restaurant (
    @field:SerializedName("restaurants")
    val articles: List<RestaurantItem?>? = null,

    @field:SerializedName("status")
    val status: String? = null
)

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