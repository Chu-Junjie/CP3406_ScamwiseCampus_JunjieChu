package com.chujunjie.scamwisecampus.ui.navigation

sealed class AppRoute(val route: String) {
    object Home : AppRoute("home")
    object Practice : AppRoute("practice")
    object Statistics : AppRoute("statistics")
    object Settings : AppRoute("settings")
}