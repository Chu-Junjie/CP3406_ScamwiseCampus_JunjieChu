package com.chujunjie.scamwisecampus.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chujunjie.scamwisecampus.ui.navigation.AppNavigation
import com.chujunjie.scamwisecampus.ui.navigation.ScamWiseBottomBar
import com.chujunjie.scamwisecampus.ui.navigation.topLevelDestinations

@Composable
fun ScamWiseApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar =
        topLevelDestinations.any { destination ->
            destination.route == currentRoute
        }
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                ScamWiseBottomBar(
                    destinations = topLevelDestinations,
                    currentRoute = currentRoute,
                    onDestinationClick = { destination ->
                        navController.navigate(destination.route) {
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
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}