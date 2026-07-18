package com.chujunjie.scamwisecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chujunjie.scamwisecampus.ui.screens.home.HomeScreen
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeRoute
import com.chujunjie.scamwisecampus.ui.screens.settings.SettingsScreen
import com.chujunjie.scamwisecampus.ui.screens.statistics.StatisticsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = modifier
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(
                onPracticeClick = {
                    navController.navigate(AppRoute.Practice.route) {
                        launchSingleTop = true
                    }
                },
                onStatisticsClick = {
                    navController.navigate(AppRoute.Statistics.route) {
                        launchSingleTop = true
                    }
                },
                onSettingsClick = {
                    navController.navigate(AppRoute.Settings.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoute.Practice.route) {
            PracticeRoute()
        }

        composable(AppRoute.Statistics.route) {
            StatisticsScreen()
        }

        composable(AppRoute.Settings.route) {
            SettingsScreen()
        }
    }
}