package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeDashboardCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}

@Composable
internal fun HomeSummaryRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Read each summary label and value together.
            },
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
internal fun HomeMessageContent(
    message: String,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        if (showProgress) {
            CircularProgressIndicator()
        }

        Text(
            text = message,
            style =
                MaterialTheme.typography.bodyLarge,
            modifier = if (showProgress) {
                Modifier.padding(top = 16.dp)
            } else {
                Modifier
            }
        )
    }
}