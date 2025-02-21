package com.example.foodplanner.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodplanner.utils.AuthState
import com.example.foodplanner.utils.SharedPrefManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class AuthViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private fun onAuthSuccess(uid: String) {
        SharedPrefManager.saveUserUID(uid)
        _authState.value = AuthState.AUTH_SUCCESS
    }

    private fun onAuthFailed() {
        _authState.value = AuthState.AUTH_FAILED
    }

    private fun onAuthInProgress() {
        _authState.value = AuthState.AUTH_IN_PROGRESS
    }

    fun signUp(email: String, password: String) {
        onAuthInProgress()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    onAuthSuccess(uid)
                    Log.d("AuthViewModel", "createUserWithEmail:success")
                } else {
                    onAuthFailed()
                    _errorMessage.value = task.exception?.message
                    Log.w("AuthViewModel", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    fun signIn(email: String, password: String) {
        onAuthInProgress()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    onAuthSuccess(uid)
                    Log.d("AuthViewModel", "signInWithEmail:success")
                } else {
                    onAuthFailed()
                    _errorMessage.value = task.exception?.message
                    Log.w("AuthViewModel", "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun signOut() {
        try {
            onAuthInProgress()
            auth.signOut()
            SharedPrefManager.clearUserUID()
            _authState.value = AuthState.AUTH_SUCCESS
        } catch (e: Exception) {
            onAuthFailed()
            _errorMessage.value = e.message.toString()
            Log.w("AuthViewModel", "signOut:failure", e)
        }
    }

    fun isUserSignedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }
}