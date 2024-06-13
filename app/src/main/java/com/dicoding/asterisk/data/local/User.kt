package com.dicoding.asterisk.data.local

data class User (
    val username: String,
    val fullName: String,
    val email: String,
    val token: String,
    val isLoggedIn: Boolean = false
)