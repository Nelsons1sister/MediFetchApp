package com.example.mymedifetch.navigation

sealed class Screen (val route: String){
    object Landing : Screen ("landing")
    object ChooseAccount : Screen("choose_account")

    object  Login: Screen("Login")

    object CreateAccount : Screen("create_account")

    object Dashboard : Screen("dashboard")
}