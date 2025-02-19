package com.example.mealbridge

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mealbridge.Screens.BottomNavBar
import com.example.mealbridge.Screens.BottomNavItem
import com.example.mealbridge.Screens.Donate
import com.example.mealbridge.Screens.Home
import com.example.mealbridge.Screens.Offers
import com.example.mealbridge.Screens.Profile


@Composable
fun navigations() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        NavHost(navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)) {
            composable(BottomNavItem.Home.route) {
                Home()
            }
            composable(BottomNavItem.Donate.route) {
                Donate(navController)
            }
            composable(BottomNavItem.Offer.route) {
                Offers()
            }
            composable(BottomNavItem.Profile.route) {
                Profile()
            }
        }
    }
}
