package com.example.mymedifetchproject.data

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,           // This will be the UUID from Supabase Auth
    val full_name: String,
    val email: String,
    val role: String,         // 'patient' or 'doctor'
    val phone_number: String? = null
)

