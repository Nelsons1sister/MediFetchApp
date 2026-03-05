package com.example.mymedifetchproject.data

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

/**
 * NOTE: The UserProfile data class is already defined in AuthRepository.kt
 * We do not redefine it here to avoid "Conflicting Overloads" or "Redeclaration" errors.
 */

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository(
        firebaseAuth = FirebaseAuth.getInstance(),
        supabase = SupabaseManager.client
    )

    val currentUser: FirebaseUser? get() = FirebaseAuth.getInstance().currentUser

    // --- STATE MANAGEMENT ---
    private val _currentUserRole = mutableStateOf<String?>(null)
    val currentUserRole: State<String?> = _currentUserRole

    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> = _userProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _fieldErrors = mutableStateOf<Map<String, String>>(emptyMap())
    val fieldErrors: State<Map<String, String>> = _fieldErrors

    // --- HELPERS ---
    fun clearError() {
        _errorMessage.value = null
        _fieldErrors.value = emptyMap()
        _isLoading.value = false
    }

    fun clearFieldError(field: String) {
        val current = _fieldErrors.value.toMutableMap()
        current.remove(field)
        _fieldErrors.value = current
    }

    private fun validateInputs(
        email: String? = null,
        pass: String? = null,
        name: String? = null,
        phone: String? = null
    ): Boolean {
        val errors = mutableMapOf<String, String>()

        email?.let {
            if (it.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                errors["email"] = "Please enter a valid email address"
            }
        }
        pass?.let {
            if (it.length < 6) {
                errors["password"] = "Password must be at least 6 characters"
            }
        }
        name?.let {
            if (it.trim().length < 3) {
                errors["name"] = "Name must be at least 3 characters"
            }
        }
        phone?.let {
            if (it.length < 10) {
                errors["phone"] = "Enter a valid 10-digit phone number"
            }
        }

        _fieldErrors.value = errors
        return errors.isEmpty()
    }

    // --- CORE LOGIC ---

    fun fetchUserProfile() {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val profile = repository.getProfile(uid)
                _userProfile.value = profile
                if (profile != null) {
                    // Check both role and user_type for the dashboard
                    _currentUserRole.value = profile.user_type ?: profile.role
                }
            } catch (e: Exception) {
                _errorMessage.value = "Unable to sync profile data."
            }
        }
    }

    fun sendPasswordReset(email: String, onComplete: (Boolean, String?) -> Unit) {
        if (!validateInputs(email = email)) {
            onComplete(false, "Invalid email format.")
            return
        }

        _isLoading.value = true
        _fieldErrors.value = emptyMap()

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _errorMessage.value = "Reset link sent! Check your inbox."
                    onComplete(true, null)
                } else {
                    val msg = task.exception?.message ?: "We couldn't find an account with this email."
                    _fieldErrors.value = mapOf("email" to msg)
                    onComplete(false, msg)
                }
            }
    }

    fun resetPassword(email: String, onComplete: (Boolean) -> Unit) {
        sendPasswordReset(email) { success, _ -> onComplete(success) }
    }

    fun saveProfile(name: String, phone: String, onComplete: (Boolean) -> Unit) {
        if (!validateInputs(name = name, phone = phone)) {
            onComplete(false)
            return
        }

        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            // Uses the repository function that maps name and phone_number
            val result = repository.updateProfile(uid, name, phone)

            result.onSuccess {
                fetchUserProfile() // Refresh state so Dashboard updates
                _isLoading.value = false
                onComplete(true)
            }
            result.onFailure { e ->
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Failed to save profile."
                onComplete(false)
            }
        }
    }

    fun login(email: String, pass: String, expectedRole: String, onRoleFound: (String) -> Unit) {
        if (!validateInputs(email = email, pass = pass)) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _fieldErrors.value = emptyMap()

            val result = repository.signInUser(email, pass)

            result.onSuccess {
                val role = repository.getUserRole()

                if (role != null && role.lowercase() == expectedRole.lowercase()) {
                    _currentUserRole.value = role
                    fetchUserProfile()
                    _isLoading.value = false
                    onRoleFound(role)
                } else if (role != null) {
                    repository.signOut()
                    _isLoading.value = false
                    _fieldErrors.value = mapOf("email" to "Account mismatch: Registered as $role.")
                } else {
                    _errorMessage.value = "User profile not found."
                    _isLoading.value = false
                }
            }
            result.onFailure { e ->
                _isLoading.value = false
                val msg = e.message ?: ""
                when {
                    msg.contains("password", true) || msg.contains("credential", true) -> {
                        _fieldErrors.value = mapOf("password" to "Incorrect password.")
                    }
                    msg.contains("user", true) || msg.contains("email", true) -> {
                        _fieldErrors.value = mapOf("email" to "Email address not found.")
                    }
                    else -> {
                        _errorMessage.value = "Login failed. Please try again."
                    }
                }
            }
        }
    }

    fun signUp(email: String, pass: String, name: String, phone: String, role: String, onSuccess: () -> Unit) {
        if (!validateInputs(email, pass, name, phone)) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.signUpUser(email, pass, name, phone, role)

            result.onSuccess {
                _currentUserRole.value = role
                fetchUserProfile()
                _isLoading.value = false
                onSuccess()
            }
            result.onFailure { e ->
                _isLoading.value = false
                val msg = e.message ?: ""
                if (msg.contains("already", true)) {
                    _fieldErrors.value = mapOf("email" to "This email is already registered.")
                } else {
                    _errorMessage.value = "Registration failed."
                }
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
                        _currentUserRole.value = role
                        fetchUserProfile()
                        onSessionFound(role)
                    } else { onNoSession() }
                } catch (e: Exception) { onNoSession() }
                finally { _isLoading.value = false }
            }
        } else { onNoSession() }
    }

    fun logout(onLogoutComplete: () -> Unit = {}) {
        repository.signOut()
        _currentUserRole.value = null
        _userProfile.value = null
        _errorMessage.value = null
        _isLoading.value = false
        onLogoutComplete()
    }
}