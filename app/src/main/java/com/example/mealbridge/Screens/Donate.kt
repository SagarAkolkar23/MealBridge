package com.example.mealbridge.Screens

import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.mealbridge.R
import java.io.File
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Donate(){

    val foodDescription = remember { mutableStateOf("") }
    val foodQuantity = remember { mutableStateOf("") }
    val addressDetails = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var time by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val type = listOf("Veg", "Non-Veg", "Both")
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Food Type*") }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                Log.d("CameraIntent", "Photo saved: $photoUri")
            } else {
                Log.e("CameraIntent", "Photo capture failed")
            }
        }
    )




    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Donate Food",
                color = Color.White,
                fontSize = 20.sp) },
            navigationIcon = {
                IconButton(onClick = { /* Handle navigation icon press */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Menu")
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.NavBar),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,

            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                OutlinedTextField(
                    value = number.value,
                    onValueChange = { number.value = it },
                    label = { Text("Contact Details*") },
                    modifier = Modifier,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.NavBar),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Black,
                        textColor = Color.LightGray,
                        focusedLabelColor = colorResource(R.color.NavBar),
                        unfocusedBorderColor = colorResource(R.color.BlueButton)
                    )
                )
            }
            item {
                textField("Address Details*", addressDetails, focusManager, ImeAction.Next)
            }
            item {
                textField("Food Description*", foodDescription, focusManager, ImeAction.Next)
            }
            item {
                textField("Food Quantity*", foodQuantity, focusManager, ImeAction.Done)
            }
            item {
                Text(
                    "*Enter food quantity in detail\n  Example: Roti -> 10 nos.; Rice -> 5 kg; Dal -> 10 kg",
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
            item {
                OutlinedButton(
                    onClick = {
                        showTimePicker(context) { hour, minute ->
                            time = "$hour : $minute"
                        }
                    },
                    modifier = Modifier
                        .border(1.dp, colorResource(R.color.BlueButton), shape = RoundedCornerShape(5.dp))
                        .width(280.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text("Select Pick Up Time*")
                }
            }
            item {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .border(1.dp, colorResource(R.color.BlueButton), shape = RoundedCornerShape(5.dp))
                        .width(280.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray,
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text(selectedCountry)
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Icon"
                    )
                }
            }
            item {
                DropdownMenu(
                    expanded = expanded,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    onDismissRequest = { expanded = false }
                ) {
                    type.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedCountry = type
                                expanded = false
                            }
                        )
                    }
                }
            }
            item {
                OutlinedButton(
                    onClick = {
                        val photoFile = createImageFile(context) { uri ->
                            photoUri = uri
                        }
                        photoUri?.let { cameraLauncher.launch(it) }
                    },
                    modifier = Modifier
                        .border(1.dp, colorResource(R.color.BlueButton), shape = RoundedCornerShape(15.dp))
                        .width(280.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray,
                        backgroundColor = Color.Gray
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = null
                    )
                }
            }
            item {
                if (photoUri != null) {
                    Text(
                        text = "Photo saved at: $photoUri",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                Button(
                    onClick = { },
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.BlueButton)
                    )
                ) {
                    Text("Submit")
                }
            }
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

    }

}

@Composable
fun textField(label: String, data: MutableState<String>, focusManager: FocusManager, imeAction: ImeAction){
    OutlinedTextField(value = data.value,
        onValueChange = { data.value = it },
        label = { Text(label)},
        modifier = Modifier,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = {  }),
        maxLines = 10,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.NavBar),
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black,
            textColor = Color.LightGray,
            focusedLabelColor = colorResource(R.color.NavBar),
            unfocusedBorderColor = colorResource(R.color.BlueButton)
        )
    )
}

fun showTimePicker(context: Context, onTimeSelected: (Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        onTimeSelected(selectedHour, selectedMinute)
    }, hour, minute, true)

    timePickerDialog.show()
}


fun createImageFile(context: Context, onUriCreated: (Uri) -> Unit): File {

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir = context.externalCacheDir
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save the file path in the photoUri state
        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            this
        )
        onUriCreated(photoUri)
    }
}


