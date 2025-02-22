package com.example.mealbridge

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

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
        val file = getFileFromUri(context, imageUri)
        uploadImageToCloudinary(file, { imageUrl ->
            val foodDetails = Food_Details(id, contact, address, description, quantity, foodType, pickupTime, imageUrl, latitude, longitude)
            database.child(id).setValue(foodDetails)
                .addOnSuccessListener {
                    Toast.makeText(context, "Food details uploaded!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("UploadFoodDetails", "Database upload failed", e)
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }, { error ->
            Log.e("UploadFoodDetails", "Image upload failed", error)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Image upload failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

fun uploadImageToCloudinary(imageFile: File, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
    val cloudName = "dzamhdt9a"  // Ensure this is correct
    val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
    val uploadPreset = "MealBridge"  // Ensure this preset exists in Cloudinary

    val client = OkHttpClient()

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull()))
        .addFormDataPart("upload_preset", uploadPreset)  // Cloudinary requires this
        .build()

    val request = Request.Builder()
        .url(uploadUrl)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            onError(e)
        }

        override fun onResponse(call: okhttp3.Call, response: Response) {
            if (response.isSuccessful) {
                response.body?.string()?.let { responseBody ->
                    val json = JSONObject(responseBody)
                    val imageUrl = json.getString("secure_url")  // Get the secure image URL
                    onSuccess(imageUrl)
                }
            } else {
                val errorMsg = response.body?.string() ?: "Unknown error"
                onError(IOException("Upload failed: $errorMsg"))
            }
        }
    })
}



fun getFileFromUri(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    } ?: "temp_file"

    val file = File(context.cacheDir, fileName)
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(file)

    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()

    return file
}

fun fetchFoodImage(foodId: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("FoodDonations").child(foodId)

    database.get().addOnSuccessListener { snapshot ->
        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
        if (!imageUrl.isNullOrEmpty()) {
            onSuccess(imageUrl)
        } else {
            onError(Exception("Image URL not found"))
        }
    }.addOnFailureListener { error ->
        onError(error)
    }
}

