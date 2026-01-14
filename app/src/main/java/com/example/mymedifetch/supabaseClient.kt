package com.example.mymedifetch

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.installPostgrest
import io.github.jan.supabase.installAuth

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://iqebvyikwprgaxrzohvs.supabase.co",
        supabaseKey = "sb_publishable_NQr08bEbiIMkvsy8jBJ9FQ_8vfzQPcq"
    ) {
        installPostgrest()    // install Postgrest module
        installAuth()         // install Auth module
    }
}
