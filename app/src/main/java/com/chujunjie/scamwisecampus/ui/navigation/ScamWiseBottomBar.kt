package com.chujunjie.scamwisecampus.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun ScamWiseBottomBar(
    destinations: List<TopLevelDestination>,
    currentRoute: String?,
    onDestinationClick: (TopLevelDestination) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val label = stringResource(destination.labelResourceId)

            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    onDestinationClick(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(text = label)
                }
            )
        }
    }
}