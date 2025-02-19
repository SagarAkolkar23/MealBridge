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
import com.example.mealbridge.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavBar(){
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = colorResource(R.color.NavBar),
                contentColor = colorResource(R.color.lightGray)
            ) {
                val items = listOf("Home", "Donate", "Offer", "Profile")
                val icons = listOf(
                    Icons.Default.Home,
                    painterResource(R.drawable.food),
                    Icons.Default.ShoppingCart,
                    Icons.Default.Person
                )
                items.forEachIndexed{ index, item ->
                    BottomNavigationItem(
                        icon = { if (index == 1) {
                            Icon(painter = icons[index] as Painter, contentDescription = null, modifier = Modifier.size(26.dp))
                        } else {
                            Icon(imageVector = icons[index] as ImageVector, contentDescription = null)
                        }},
                        label = { Text(item) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index}
                    )
                }
            }
        }
    )
    {
        innerPadding ->
        when(selectedIndex){
            0 -> Home()
            1 -> Donate()
            2 -> Offers()
            3 -> Profile()
        }
    }
}