package com.example.mymedifetchproject

sealed class Screen(val route: String) {
    // 🚪 AUTH / ENTRANCE
    object Splash : Screen("splash")
    object Landing : Screen("landing")
    object AccountSelect : Screen("account_select")

    // Dynamic Login logic
    object Login : Screen("login/{role}") {
        fun createRoute(role: String) = "login/$role"
    }

    // Recovery Screens
    object ForgotPasswordPatient : Screen("forgot_password_patient")
    object ForgotPasswordProvider : Screen("forgot_password_provider")

    // Dynamic Route for Account Creation
    object CreateAccount : Screen("create_account/{role}") {
        fun createRoute(role: String) = "create_account/$role"
    }

    // 🏥 PATIENT ECOSYSTEM
    object PatientDashboard : Screen("patient_dashboard")
    object PatientHome : Screen("patient_home")
    object FindLabs : Screen("find_labs")
    object PatientReports : Screen("patient_reports")
    object PatientProfile : Screen("patient_profile")
    object ReportSickness : Screen("report_sickness")
    object Prescription : Screen("prescription")

    // ✅ FIXED: Added to handle clicking cards in the Reports tab
    object PatientReportDetail : Screen("patient_report_detail/{reportId}") {
        fun createRoute(reportId: String) = "patient_report_detail/$reportId"
    }

    // Check-in logic (Matches your FindLabs navigate call)
    object LabCheckIn : Screen("lab_checkin/{name}/{address}") {
        fun createRoute(name: String, address: String) = "lab_checkin/$name/$address"
    }

    // 🩺 PROVIDER ECOSYSTEM
    object ProviderDashboard : Screen("provider_dashboard")
    object ProviderHome : Screen("provider_home")
    object ProviderProfile : Screen("provider_profile")

    // 📋 PROVIDER TOOLS & MANAGEMENT
    object PatientList : Screen("patient_list")

    // View specific patient details
    object PatientDetail : Screen("patient_detail/{patientId}") {
        fun createRoute(patientId: String) = "patient_detail/$patientId"
    }

    // --- 🚀 THE "LOOP" CONNECTORS ---

    // 1. Lab Waiting Room (For Lab Techs to see checked-in patients)
    object LabWaitingRoom : Screen("lab_waiting_room")

    // 2. Provider Lab Inbox (For Doctors to see finished results)
    object ProviderLabInbox : Screen("provider_lab_inbox")

    // 3. ProviderPrescription accepts an ID to link to a specific report
    object ProviderPrescription : Screen("provider_prescription/{patientId}") {
        fun createRoute(patientId: String) = "provider_prescription/$patientId"
    }

    // 4. LabUpload accepts a caseId to link the file to the specific sample
    object LabUpload : Screen("lab_upload/{caseId}") {
        fun createRoute(caseId: String) = "lab_upload/$caseId"
    }
}