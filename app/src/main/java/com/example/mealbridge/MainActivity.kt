package com.example.mealbridge

import NavGraph
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mealbridge.Screens.BottomNavBar
import com.example.mealbridge.ui.theme.MealBridgeTheme
import android.Manifest
import android.os.Build
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value

            if (isGranted) {
                Log.d("Permissions", "$permission granted")
            } else {
                Log.e("Permissions", "$permission denied")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requiredPermissions = mutableListOf(
            Manifest.permission.CAMERA
        )

        requiredPermissions.add(Manifest.permission.READ_MEDIA_IMAGES) // Android 13+

        // Check permissions and request if not granted
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            Log.d("Permissions", "All required permissions already granted")
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MealBridgeTheme {
                NavGraph(navController)
            }
        }
    }
}