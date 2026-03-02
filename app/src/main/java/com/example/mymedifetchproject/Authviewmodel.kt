package com.example.mymedifetchproject.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository(
        firebaseAuth = FirebaseAuth.getInstance(),
        supabase = SupabaseManager.client
    )

    val currentUser: FirebaseUser? get() = FirebaseAuth.getInstance().currentUser

    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> = _userProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    /**
     * Resets the error and loading states.
     * Call this when the user starts typing to "unlock" the UI.
     */
    fun clearError() {
        _errorMessage.value = null
        _isLoading.value = false
    }

    fun fetchUserProfile() {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val profile = repository.getProfile(uid)
                _userProfile.value = profile
            } catch (e: Exception) {
                _errorMessage.value = "Unable to sync profile data: ${e.message}"
            }
        }
    }

    fun saveProfile(name: String, phone: String, onComplete: (Boolean) -> Unit) {
        val uid = currentUser?.uid ?: return

        // --- Local Validation ---
        if (name.isBlank() || phone.isBlank()) {
            _errorMessage.value = "Name and Phone cannot be empty."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.updateProfile(uid, name, phone)

            _isLoading.value = false
            result.onSuccess {
                fetchUserProfile()
                onComplete(true)
            }
            result.onFailure { e ->
                _errorMessage.value = e.message ?: "Failed to save profile."
                onComplete(false)
            }
        }
    }

    fun checkUserSession(onSessionFound: (String) -> Unit, onNoSession: () -> Unit) {
        val user = currentUser
        if (user != null) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val role = repository.getUserRole()
                    if (role != null) {
                        fetchUserProfile()
                        onSessionFound(role)
                    } else {
                        onNoSession()
                    }
                } catch (e: Exception) {
                    onNoSession()
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            onNoSession()
        }
    }

    fun login(email: String, pass: String, onRoleFound: (String) -> Unit) {
        // --- Local Validation ---
        if (email.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Please enter both email and password."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.signInUser(email, pass)

            result.onSuccess {
                val role = repository.getUserRole()
                if (role != null) {
                    fetchUserProfile()
                    onRoleFound(role)
                } else {
                    _errorMessage.value = "User profile not found in database."
                    _isLoading.value = false
                }
            }
            result.onFailure { e ->
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Login failed. Check your credentials."
            }
        }
    }

    fun signUp(email: String, pass: String, name: String, phone: String, role: String, onSuccess: () -> Unit) {
        // --- Local Validation: Check for "Empty Columns" ---
        if (email.isBlank() || pass.isBlank() || name.isBlank() || phone.isBlank()) {
            _errorMessage.value = "All fields are required to create an account."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.signUpUser(email, pass, name, phone, role)

            _isLoading.value = false
            result.onSuccess {
                fetchUserProfile()
                onSuccess()
            }
            result.onFailure { e ->
                // Ensures the button is "un-stuck" so user can fix the error
                _errorMessage.value = e.message ?: "Registration failed."
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit = {}) {
        repository.signOut()
        _userProfile.value = null
        _errorMessage.value = null
        _isLoading.value = false
        onLogoutComplete()
    }
}