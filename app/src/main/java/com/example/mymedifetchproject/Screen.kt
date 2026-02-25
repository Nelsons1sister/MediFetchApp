package com.example.mymedifetchproject

sealed class Screen(val route: String) {
    // 🚪 AUTH / ENTRANCE
    object Splash : Screen("splash")
    object Landing : Screen("landing")
    object AccountSelect : Screen("account_select")

    object Login : Screen("login/{role}") {
        fun createRoute(role: String) = "login/$role"
    }

    object CreateAccount : Screen("create_account/{role}") {
        fun createRoute(role: String) = "create_account/$role"
    }

    // 🏥 PATIENT ECOSYSTEM
    object PatientDashboard : Screen("patient_dashboard")
    object PatientHome : Screen("patient_dashboard")
    object FindLabs : Screen("find_labs")
    object PatientReports : Screen("patient_reports")
    object PatientProfile : Screen("patient_profile")
    object ReportSickness : Screen("report_sickness")

    // ✅ ADDED BACK: This fixes the error in PatientMainScreen
    object LabCheckIn : Screen("lab_checkin/{name}/{address}") {
        fun createRoute(name: String, address: String) = "lab_checkin/$name/$address"
    }

    object PatientReportDetail : Screen("patient_report_detail/{reportId}") {
        fun createRoute(reportId: String) = "patient_report_detail/$reportId"
    }

    // 🩺 PROVIDER ECOSYSTEM
    object ProviderDashboard : Screen("provider_dashboard")

    // ✅ MATCHES NAVGRAPH: Lab Waiting Room (formerly Check-In UI)
    object LabWaitingRoom : Screen("lab_waiting_room")

    // ✅ MATCHES NAVGRAPH: Lab Results Inbox
    object ProviderLabInbox : Screen("lab_reports_inbox")

    // ✅ MATCHES NAVGRAPH: Prescription logic
    object ProviderPrescription : Screen("provider_prescription/{patientId}") {
        fun createRoute(patientId: String) = "provider_prescription/$patientId"
    }

    // ✅ MATCHES NAVGRAPH: Patient Details
    object PatientDetail : Screen("patient_detail/{patientId}") {
        fun createRoute(patientId: String) = "patient_detail/$patientId"
    }

    // Legacy/Unused
    object ForgotPasswordPatient : Screen("forgot_password_patient")
    object ForgotPasswordProvider : Screen("forgot_password_provider")
}