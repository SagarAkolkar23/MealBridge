package com.example.mealbridge

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

fun UploadFoodDetails(
    contact: String,
    address: String,
    description: String,
    quantity: String,
    foodType: String,
    pickupTime: String,
    imageUri: Uri?,
    latitude: Double,
    longitude: Double,
    context: Context
) {
    val database = FirebaseDatabase.getInstance().getReference("FoodDonations")
    val id = database.push().key ?: return

    if (imageUri != null) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images").child("$id.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("UploadFoodDetails", "Image uploaded successfully: $uri")

                    val foodDetails = Food_Details(id, contact, address, description, quantity, foodType, pickupTime, uri.toString(), latitude, longitude)
                    database.child(id).setValue(foodDetails)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Food details uploaded!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e("UploadFoodDetails", "Database upload failed", e)
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("UploadFoodDetails", "Image upload failed", e)
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        val foodDetails = Food_Details(id, contact, address, description, quantity, foodType, pickupTime, "", latitude, longitude)

        database.child(id).setValue(foodDetails)
            .addOnSuccessListener {
                Toast.makeText(context, "Food details uploaded!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("UploadFoodDetails", "Database upload failed", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}