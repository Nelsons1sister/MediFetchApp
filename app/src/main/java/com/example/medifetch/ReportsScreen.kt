//package com.example.medifetch
//
//import androidx.compose.runtime.Composable
//
//@Composable
//fun ReportsScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//
//        // Header
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFF2E7D6F)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("JD", color = Color.White, fontWeight = FontWeight.Bold)
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column {
//                Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                Text("Patient ID: #12345", color = Color.Gray)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyColumn {
//            items(reports) { report ->
//                ReportCard(report)
//            }
//        }
//    }
//}
