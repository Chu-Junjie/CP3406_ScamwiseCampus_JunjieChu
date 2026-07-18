package com.chujunjie.scamwisecampus.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.chujunjie.scamwisecampus.R

data class TopLevelDestination(
    val route: String,
    @StringRes val labelResourceId: Int,
    val icon: ImageVector
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        route = AppRoute.Home.route,
        labelResourceId = R.string.navigation_home,
        icon = Icons.Filled.Home
    ),
    TopLevelDestination(
        route = AppRoute.Practice.route,
        labelResourceId = R.string.navigation_practice,
        icon = Icons.Filled.School
    ),
    TopLevelDestination(
        route = AppRoute.Statistics.route,
        labelResourceId = R.string.navigation_statistics,
        icon = Icons.Filled.BarChart
    ),
    TopLevelDestination(
        route = AppRoute.Settings.route,
        labelResourceId = R.string.navigation_settings,
        icon = Icons.Filled.Settings
    )
)