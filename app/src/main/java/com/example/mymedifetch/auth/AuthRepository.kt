package com.example.mymedifetch.auth

import com.example.mymedifetch.SupabaseClient
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {

    private val auth = SupabaseClient.client.auth

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            val provider = Email(email = email, password = password)
            auth.signUpWith(provider)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val provider = Email(email = email, password = password)
            auth.signInWith(provider)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
