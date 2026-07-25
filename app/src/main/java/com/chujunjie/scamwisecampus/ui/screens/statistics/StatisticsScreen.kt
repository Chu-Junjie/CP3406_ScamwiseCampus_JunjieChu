package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState,
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> {
                LoadingStatisticsContent(
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.error != null -> {
                StatisticsMessageContent(
                    message = stringResource(
                        uiState.error.messageRes()
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            !uiState.hasAttempts -> {
                EmptyStatisticsContent(
                    onPracticeClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                StatisticsDashboard(
                    uiState = uiState,
                    onPracticeClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}