package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R

@Composable
internal fun LoadingStatisticsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = stringResource(
                R.string.statistics_loading
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
internal fun EmptyStatisticsContent(
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
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
        Text(
            text = stringResource(
                R.string.statistics_no_history_title
            ),
            style =
                MaterialTheme.typography.headlineSmall,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = stringResource(
                R.string.statistics_no_history_description
            ),
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = onPracticeClick,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.statistics_start_practice
                )
            )
        }
    }
}

@Composable
internal fun StatisticsMessageContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                liveRegion =
                    LiveRegionMode.Assertive
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.statistics_unavailable
            ),
            style =
                MaterialTheme.typography.headlineSmall,
            color =
                MaterialTheme.colorScheme.error,
            modifier = Modifier.semantics {
                heading()
            }
        )

        Text(
            text = message,
            style =
                MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}