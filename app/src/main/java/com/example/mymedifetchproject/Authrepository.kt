package com.example.mymedifetchproject.data

import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable

/**
 * UserProfile: Data model for the 'profiles' table in Supabase.
 * @Serializable allows Supabase to decode the database rows into Kotlin objects.
 */
@Serializable
data class UserProfile(
    val id: String,
    val full_name: String? = null,
    val email: String? = null,
    val phone_number: String? = null,
    val role: String? = null,
    val user_type: String? = null
)

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val supabase: SupabaseClient
) {

    /**
     * 🔍 FETCH PROFILE
     * Grabs the specific profile row from Supabase using the unique Firebase UID.
     */
    suspend fun getProfile(uid: String): UserProfile? {
        return try {
            supabase.from("profiles")
                .select {
                    filter { eq("id", uid) }
                }.decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 🏷️ GET USER ROLE
     * Retrieves the role assigned to the user. Checks both fields for safety.
     */
    suspend fun getUserRole(): String? {
        val uid = firebaseAuth.currentUser?.uid ?: return null
        val profile = getProfile(uid)
        return profile?.user_type ?: profile?.role
    }

    /**
     * 💾 UPDATE PROFILE
     * Updates specific columns in the 'profiles' table.
     * Since RLS is disabled, this bypasses permission checks.
     */
    suspend fun updateProfile(uid: String, name: String, phone: String): Result<Unit> {
        return try {
            supabase.from("profiles").update(
                {
                    set("full_name", name)
                    set("phone_number", phone)
                }
            ) {
                filter { eq("id", uid) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 🔑 SIGN IN
     * Authenticates user via Firebase Email/Password.
     */
    suspend fun signInUser(email: String, pass: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 📝 SIGN UP (Updated)
     * 1. Creates a user account in Firebase.
     * 2. Upserts a profile row in Supabase including the PHONE NUMBER.
     */
    suspend fun signUpUser(
        email: String,
        pass: String,
        name: String,
        phone: String, // ✅ Added phone parameter
        role: String
    ): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("Firebase UID not found")

            val profile = UserProfile(
                id = uid,
                full_name = name,
                email = email,
                phone_number = phone, // ✅ Mapping phone to the database object
                role = role,
                user_type = role
            )

            // Upsert ensures the profile is created even if there's a minor sync delay
            supabase.from("profiles").upsert(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 🚪 SIGN OUT
     * Ends the Firebase session.
     */
    fun signOut() {
        firebaseAuth.signOut()
    }
}