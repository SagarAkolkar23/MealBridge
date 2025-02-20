package com.example.mealbridge.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil3.compose.AsyncImage
import com.example.mealbridge.Authentication.GoogleAuthUiClient
import com.example.mealbridge.R
import com.google.android.gms.auth.api.identity.Identity




@Composable
fun Profile(
    onSignOut :() -> Unit
){
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val user = googleAuthUiClient.getSignedInUser()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text("Hello, ${user?.name}",
                fontSize = 25.sp)
            Button(
                onSignOut,
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.calmingGreen)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)  // Adjust the icon size to fit within the button
                )
            }

        }

        val painter = rememberImagePainter(data = user?.profile)




        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
                .border(width = 2.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = colorResource(R.color.calmingGreen))
                .clip(RoundedCornerShape(15.dp))
        ){
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Row() {
                    Image(
                        painter = painterResource(R.drawable.baseline_info_outline_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text("Personal Information")
                }
                Divider(
                    color = colorResource(R.color.calmingGreen),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(R.color.calmingGreen), CircleShape)
                )
                Spacer(modifier = Modifier.height(15.dp))

                Text("Name : ${user?.name}",
                    fontSize = 20.sp)
                Spacer(modifier = Modifier.height(15.dp))
                Text("Email : ${user?.email}",
                    fontSize = 20.sp)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}