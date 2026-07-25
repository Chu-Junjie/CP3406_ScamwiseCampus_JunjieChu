package com.chujunjie.scamwisecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chujunjie.scamwisecampus.ui.screens.home.HomeRoute
import com.chujunjie.scamwisecampus.ui.screens.linkverification.LinkVerificationRoute
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeRoute
import com.chujunjie.scamwisecampus.ui.screens.scenario.ScenarioActivityRoute
import com.chujunjie.scamwisecampus.ui.screens.settings.SettingsRoute
import com.chujunjie.scamwisecampus.ui.screens.statistics.StatisticsRoute

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
            HomeRoute(
                onScenarioClick = { scenarioId ->
                    navController.navigate(
                        AppRoute.ScenarioActivity
                            .createRoute(scenarioId)
                    )
                },
                onPracticeClick = {
                    navController.navigateToTopLevelDestination(
                        AppRoute.Practice.route
                    )
                },
                onStatisticsClick = {
                    navController.navigateToTopLevelDestination(
                        AppRoute.Statistics.route
                    )
                },
                onLinkVerificationClick = {
                    navController.navigate(
                        AppRoute.LinkVerification.route
                    )
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
                    navController.navigateToTopLevelDestination(
                        AppRoute.Practice.route
                    )
                }
            )
        }

        composable(AppRoute.Settings.route) {
            SettingsRoute()
        }

        composable(
            route = AppRoute.LinkVerification.route
        ) {
            LinkVerificationRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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
                    val returnedToExistingPractice =
                        navController.popBackStack(
                            route = AppRoute.Practice.route,
                            inclusive = false
                        )

                    if (!returnedToExistingPractice) {
                        navController.navigateToTopLevelDestination(
                            AppRoute.Practice.route
                        )
                    }
                },
                onReturnHome = {
                    navController.navigateToTopLevelDestination(
                        AppRoute.Home.route
                    )
                },
                onViewStatistics = {
                    navController.navigateToTopLevelDestination(
                        AppRoute.Statistics.route
                    )
                }
            )
        }
    }
}

private fun NavHostController.navigateToTopLevelDestination(
    route: String
) {
    navigate(route) {
        popUpTo(
            graph.findStartDestination().id
        ) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}