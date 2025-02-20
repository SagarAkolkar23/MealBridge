import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mealbridge.Authentication.AuthView
import com.example.mealbridge.Authentication.GoogleAuthUiClient
import com.example.mealbridge.Authentication.signInScreen
import com.example.mealbridge.Screens.BottomNavBar
import com.example.mealbridge.Screens.BottomNavItem
import com.example.mealbridge.Screens.Donate
import com.example.mealbridge.Screens.Home
import com.example.mealbridge.Screens.Offers
import com.example.mealbridge.Screens.Profile
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    Scaffold(
        bottomBar = {
            // Show BottomNavBar only if user is signed in
            if (googleAuthUiClient.getSignedInUser() != null) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (googleAuthUiClient.getSignedInUser() != null) "main" else "auth",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Authentication flow
            navigation(startDestination = "signIn", route = "auth") {
                composable("signIn") {
                    val view = AuthView(googleAuthUiClient)
                    val state by view.state.collectAsStateWithLifecycle()
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(key1 = state.isSignedIn) {
                        if (state.isSignedIn) {
                            Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show()
                            navController.navigate("main") {
                                popUpTo("auth") { inclusive = true }
                            }
                            view.resetState()
                        }
                    }

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                coroutineScope.launch {
                                    val signInData = googleAuthUiClient.getSignInData(
                                        intent = result.data ?: return@launch
                                    )
                                    view.onSignInResult(signInData)
                                }
                            }
                        }
                    )

                    signInScreen(state = state, onClick = {
                        coroutineScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            signInIntentSender?.let {
                                launcher.launch(IntentSenderRequest.Builder(it).build())
                            }
                        }
                    })
                }
            }

            // Main app flow
            navigation(startDestination = BottomNavItem.Home.route, route = "main") {
                composable(BottomNavItem.Home.route) {
                    Home()
                }
                composable(BottomNavItem.Donate.route) {
                    Donate(navController)
                }
                composable(BottomNavItem.Offer.route) {
                    Offers()
                }
                composable(BottomNavItem.Profile.route) {
                    Profile(onSignOut = {
                        coroutineScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                            navController.navigate("auth")
                        }
                    })
                }
            }
        }
    }
}
