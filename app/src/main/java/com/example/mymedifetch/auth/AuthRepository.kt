package com.example.mymedifetch.auth

import com.example.mymedifetch.SupabaseClient
import io.github.jan.supabase.auth.auth
// CRITICAL: Ensure this is the ONLY 'Email' import in the file
import io.github.jan.supabase.auth.providers.Email

class AuthRepository {
    // This connects to the 'gotrue' / Auth module
    private val auth = SupabaseClient.client.auth

    suspend fun signUp(email: String, pass: String): Result<Unit> {
        return try {
            // Using the Email object from the Supabase library
            auth.signUpWith(Email) {
                this.email = email      // Assigns your variable 'email' to Supabase property 'email'
                this.password = pass    // Assigns your variable 'pass' to Supabase property 'password'
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, pass: String): Result<Unit> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}