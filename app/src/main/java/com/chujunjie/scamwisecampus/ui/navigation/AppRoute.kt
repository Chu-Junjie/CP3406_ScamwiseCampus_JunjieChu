package com.chujunjie.scamwisecampus.ui.navigation

sealed class AppRoute(val route: String) {

    object Home : AppRoute("home")

    object Practice : AppRoute("practice")

    object Statistics : AppRoute("statistics")

    object Settings : AppRoute("settings")

    object ScenarioActivity : AppRoute(
        route = "scenario/{$SCENARIO_ID_ARGUMENT}"
    ) {
        fun createRoute(scenarioId: String): String {
            return "scenario/$scenarioId"
        }
    }

    companion object {
        const val SCENARIO_ID_ARGUMENT = "scenarioId"
    }
}