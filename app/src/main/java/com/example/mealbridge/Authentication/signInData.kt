package com.example.mealbridge.Authentication


data class signInData(
    val data : Data?,
    val error : String?
)

data class Data(
    val id : String,
    val name : String?,
    val profile : String?
)
