package com.chujunjie.scamwisecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chujunjie.scamwisecampus.ui.screens.home.HomeScreen
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeScreen
import com.chujunjie.scamwisecampus.ui.screens.settings.SettingsScreen
import com.chujunjie.scamwisecampus.ui.screens.statistics.StatisticsScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = modifier
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(
                onPracticeClick = {
                    navController.navigate(AppRoute.Practice.route)
                },
                onStatisticsClick = {
                    navController.navigate(AppRoute.Statistics.route)
                },
                onSettingsClick = {
                    navController.navigate(AppRoute.Settings.route)
                }
            )
        }

        composable(AppRoute.Practice.route) {
            PracticeScreen()
        }

        composable(AppRoute.Statistics.route) {
            StatisticsScreen()
        }

        composable(AppRoute.Settings.route) {
            SettingsScreen()
        }
    }
}