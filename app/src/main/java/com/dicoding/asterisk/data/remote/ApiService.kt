package com.dicoding.asterisk.data.remote

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("nearby")
    fun getNearbyRestaurants(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int? = null
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
}