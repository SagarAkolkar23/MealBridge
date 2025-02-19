package com.example.mealbridge.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Shapes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mealbridge.R

@Composable
fun Home(){
    Column(modifier = Modifier
        .fillMaxSize()) {
        Text("Hello User")

    }

}

@SuppressLint("InvalidColorHexValue")
@Composable
fun card(){
    Card(modifier = Modifier
        .width(328.dp)
        .height(250.dp)
        .clip(RoundedCornerShape(25.dp))) {
        Box(modifier = Modifier.fillMaxSize()){
            Image(painter = painterResource(R.drawable.bhandara),
                contentDescription = null,
                modifier = Modifier
                    .width(328.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(25.dp)))
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

                    OutlinedButton(onClick = {  },
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
                           }
                            Column {
                               Text("FOOD TYPE:")
                           }
                            Column {
                               Text("TIME:")
                           }

                        }

                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun z(){
    Home()
}