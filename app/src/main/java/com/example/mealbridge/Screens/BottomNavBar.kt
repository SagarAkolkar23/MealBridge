package com.example.mealbridge.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mealbridge.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavBar(navController: NavController){

            BottomNavigation(
                backgroundColor = colorResource(R.color.calmingGreen),
                contentColor = colorResource(R.color.lightGray)
            ) {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Donate,
                    BottomNavItem.Offer,
                    BottomNavItem.Profile
                )
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                val icons = listOf(
                    Icons.Default.Home,
                    painterResource(R.drawable.food),
                    Icons.Default.ShoppingCart,
                    Icons.Default.Person
                )
                items.forEach{ item ->
                    BottomNavigationItem(
                        icon = { Icon(item.icon, contentDescription = item.label)},
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = { navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }}
                    )
                }
            }
}


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Donate : BottomNavItem("donate", Icons.Default.Favorite, "Donate")
    object Offer : BottomNavItem("offer", Icons.Default.ShoppingCart, "Offer")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}