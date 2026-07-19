package com.chujunjie.scamwisecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chujunjie.scamwisecampus.ui.screens.home.HomeScreen
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeRoute
import com.chujunjie.scamwisecampus.ui.screens.settings.SettingsScreen
import com.chujunjie.scamwisecampus.ui.screens.statistics.StatisticsRoute
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chujunjie.scamwisecampus.ui.screens.scenario.ScenarioActivityRoute
import androidx.navigation.NavGraph.Companion.findStartDestination

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
            PracticeRoute(
                onScenarioClick = { scenarioId ->
                    navController.navigate(
                        AppRoute.ScenarioActivity.createRoute(
                            scenarioId = scenarioId
                        )
                    )
                }
            )
        }

        composable(AppRoute.Statistics.route) {
            StatisticsRoute(
                onPracticeClick = {
                    navController.navigate(
                        AppRoute.Practice.route
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AppRoute.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = AppRoute.ScenarioActivity.route,
            arguments = listOf(
                navArgument(AppRoute.SCENARIO_ID_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val scenarioId =
                backStackEntry.arguments
                    ?.getString(AppRoute.SCENARIO_ID_ARGUMENT)
                    .orEmpty()

            ScenarioActivityRoute(
                scenarioId = scenarioId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onReturnToPractice = {
                    navController.popBackStack(
                        route = AppRoute.Practice.route,
                        inclusive = false
                    )
                },
                onReturnHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(
                            navController.graph
                                .findStartDestination()
                                .id
                        )

                        launchSingleTop = true
                    }
                },
                onViewStatistics = {
                    navController.navigate(
                        AppRoute.Statistics.route
                    ) {
                        popUpTo(
                            navController.graph
                                .findStartDestination()
                                .id
                        ) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}