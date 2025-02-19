package com.example.mealbridge.Authentication

data class authState(
    val isSignedIn : Boolean = false,
    val signInError : String? = null,
    val loading : Boolean = false
)