package com.example.mealbridge.Authentication


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthView @Inject constructor(private val googleAuthUiClient: GoogleAuthUiClient) : ViewModel() {
    private val _state = MutableStateFlow(authState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: signInData){
        _state.update { it.copy(
            isSignedIn = result.data != null,
            signInError = result.error,
            loading = false
        ) }
    }


    fun signIn(){
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                val intentSender = googleAuthUiClient.signIn()
                // Handle the intentSender (this might involve starting an Activity)
            } catch (e: Exception) {
                _state.update { it.copy(
                    loading = false,
                    signInError = e.message
                ) }
            }
        }
    }

    fun resetState(){
        _state.update { authState() }
    }
}