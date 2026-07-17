package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ScamWise Campus",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Pause. Check. Protect.",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onPracticeClick
        ) {
            Text(text = "Start Practice")
        }

        OutlinedButton(
            onClick = onStatisticsClick
        ) {
            Text(text = "View Statistics")
        }

        OutlinedButton(
            onClick = onSettingsClick
        ) {
            Text(text = "Open Settings")
        }
    }
}