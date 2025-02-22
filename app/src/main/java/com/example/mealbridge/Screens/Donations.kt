package com.example.mealbridge.Screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil.decode.ImageSource
import com.example.mealbridge.Food_Details

@Composable
fun Donations(food : Food_Details){

    Text(food.contact)
    Text(food.address)
    Text(food.description)
    Text(food.quantity)
    Text(food.foodType)
    Text(food.pickupTime)



}