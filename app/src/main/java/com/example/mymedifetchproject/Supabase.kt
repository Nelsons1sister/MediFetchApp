package com.example.mymedifetchproject.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseManager {
    val client = createSupabaseClient(
        supabaseUrl = "https://zlvuvekhrwzsheqmkevh.supabase.co", // Replace with your URL
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpsdnV2ZWtocnd6c2hlcW1rZXZoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU0NDc3OTUsImV4cCI6MjA4MTAyMzc5NX0.tOeWsBrNHl9IKNhLqwAzzk2hmTQzvguIu78YBK9iLQU"                        // Replace with your Key
    ) {
        install(Auth)
        install(Postgrest)
    }
}