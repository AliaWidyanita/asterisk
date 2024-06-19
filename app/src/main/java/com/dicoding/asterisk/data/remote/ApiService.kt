package com.dicoding.asterisk.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("nearby")
    fun getNearbyRestaurants(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int? = null // Optional parameter, will use default value if not included
    ): Call<List<RestaurantItem>>

    @GET("search")
    fun searchRestaurants(
        @Query("location") location: String
    ): Call<List<RestaurantItem>>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userName") userName: String,
        @Field("fullName") fullName: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("identifier") identifier: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("review")
    fun submitReview(
        @Field("reviewText") reviewText: String,
        @Field("restaurant_id") restaurantId: String,
        @Field("restaurant_name") restaurantName: String,
        @Field("imageUrl") restaurantImage: String,
        @Field("username") userName: String,
        @Field("address") address : String

    ): Call<ReviewResponse>

    @POST("statistics")
    fun getStatistics(@Body authKey: Map<String, String>): Call<List<RestaurantStatisticsResponse>>
    @GET("reviewed")
    fun getUserReviews(@Query("identifier") identifier: String): Call<List<RestaurantReview>>

    @GET("detail")
    fun getRestaurantDetails(@Query("resto_id") restoId : String ): Call<RestaurantStatisticsResponse>
//    @GET("restaurant")
//    suspend fun getRestaurant(
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 20
//    ): RestaurantResponse
//
//    @Multipart
//    @POST("restaurant")
//    fun addNewReview(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody
//    ): Call<AddReviewResponse>

}
