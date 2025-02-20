package com.example.mealbridge

import android.content.Context

data class Food_Details(
    val id: String = "",
    val contact: String = "",
    val address: String = "",
    val description: String = "",
    val quantity: String = "",
    val foodType: String = "",
    val pickupTime: String = "",
    val imageUrl: String = "",
    val latitude: Double?,
    val longitude: Double?,
) {
    constructor() : this("", "", "", "", "", "", "", "", 0.0, 0.0)

}
