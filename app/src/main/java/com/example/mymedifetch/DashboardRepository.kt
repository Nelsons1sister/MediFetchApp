//package com.example.mymedifetch
//
////package com.example.mymedifetch
//
//import com.example.medifetch.SupabaseClientInstance
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//object DashboardRepository {
//
//    fun testSupabaseConnection(onResult: (Boolean, String?) -> Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = SupabaseClientInstance.client
//                    .from("reports")
//                    .select()
//                    .execute()
//
//                // 🔁 Switch back to Main thread
//                withContext(Dispatchers.Main) {
//                    if (response.error == null) {
//                        onResult(true, null)
//                    } else {
//                        onResult(false, response.error?.message)
//                    }
//                }
//
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    onResult(false, e.message)
//                }
//            }
//        }
//    }
//}
