package com.dicoding.asterisk.data.local

data class User (
    val email: String,
    val token: String,
    val isLoggedIn: Boolean = false
)