package com.example.mealbridge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun splashScreen(navController : NavController){
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000L) // 3 seconds delay
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.calmingGreen)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Image(painter = painterResource(R.drawable.dall_e_2025_02_18_08_54_30___a_logo_design_for_a_food_sharing_app_named__mealbridge___the_logo_features_a_stylized_bridge_icon_in__2196f3__trustworthy_blue__connecting_two_hands_h),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(150.dp)))


        Spacer(modifier = Modifier.height(15.dp))
        Column(modifier = Modifier
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text("Share Food, Share Love !",
                color = colorResource(R.color.lightGray),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}


@Preview
@Composable
fun m(){
    val context = LocalContext.current
    splashScreen(navController = NavController(context))
}