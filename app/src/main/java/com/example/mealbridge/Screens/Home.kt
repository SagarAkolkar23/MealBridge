package com.example.mealbridge.Screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.mealbridge.Authentication.GoogleAuthUiClient
import com.example.mealbridge.Food_Details
import com.example.mealbridge.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.FirebaseDatabase

@Composable
fun Home(){
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Food Donations", "Discount Section")

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = colorResource(R.color.calmingGreenLight),
                )
            }
        }

        when (selectedTabIndex) {
            0 -> FoodDonationsSection()
            1 -> DiscountSection()
        }
    }

}

@SuppressLint("InvalidColorHexValue", "QueryPermissionsNeeded")
@Composable
fun card(food: Food_Details){
    val foodList = remember { mutableStateListOf<Food_Details>() }

    val context = LocalContext.current
    Card(modifier = Modifier
        .width(328.dp)
        .height(250.dp)
        .clickable {

        }
        .clip(RoundedCornerShape(25.dp))) {
        Box(modifier = Modifier.fillMaxSize()){
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(food.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
            )

            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x30FFFFFF))) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(color = Color(0x0FFFFFF)),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedButton(onClick = {
                        val geoUri = Uri.parse("geo:${food.latitude},${food.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${food.latitude},${food.longitude}")
                            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                            context.startActivity(webIntent)
                        }

                    },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0x80FFFFFF),
                            contentColor = Color.White
                        )) {
                        Image(painter = painterResource(R.drawable.google_maps),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp))
                    }

                    Card(modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(70.dp)
                        .fillMaxWidth()) {
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly){
                           Column {
                               Text("UPLOADED BY:")
                               Text(food.contact)
                           }
                            Column {
                               Text("FOOD TYPE:")
                               Text(food.foodType)
                           }
                            Column {
                               Text("TIME:")
                                Text(food.pickupTime)
                           }

                        }

                    }
                }

            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FoodDonationsSection() {
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val user = googleAuthUiClient.getSignedInUser()
    val foodList = remember { mutableStateListOf<Food_Details>() }
    val databaseRef = FirebaseDatabase.getInstance().getReference("FoodDonations")

    LaunchedEffect(Unit) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            foodList.clear()
            for (foodSnapshot in snapshot.children) {
                val foodItem = foodSnapshot.getValue(Food_Details::class.java)
                foodItem?.let { foodList.add(it) }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Text("Hello,\n${user?.name}", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Food Donations:")
        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(foodList) { food ->
                card(food)
            }
        }
    }
}
@Composable
fun DiscountSection() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Discount Section Coming Soon!", fontSize = 20.sp)
    }
}


