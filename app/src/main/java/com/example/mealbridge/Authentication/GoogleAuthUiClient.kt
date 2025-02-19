package com.example.mealbridge.Authentication


import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuthUiClient(
    private val context : Context,
    private val oneTapClient :SignInClient
) {

    private val auth = Firebase.auth

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("299587948990-8onfhlp2c9omli9n8cjrk5hj7qdorctd.apps.googleusercontent.com")
                    .setSupported(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            null

        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun getSignInData(intent: Intent): signInData {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleAuthCredential).await().user
            signInData(
                data = user?.run{
                    Data(
                        id = uid,
                        name = displayName,
                        profile = photoUrl?.toString()
                    )
                },
                error = null
            )
        }
        catch(e : Exception){
            if(e is CancellationException) throw e
            signInData(
                data = null,
                error = e.message
            )
        }
    }

    suspend fun signOut(){
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }
        catch(e : Exception){
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): Data? = auth.currentUser?.run{
        Data(
            id = uid,
            name = displayName,
            profile = photoUrl?.toString()
        )
    }

}