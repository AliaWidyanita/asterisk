package com.dicoding.asterisk.data.remote

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("fullName")
    val fullName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("session_id")
    val token: String? = null
)