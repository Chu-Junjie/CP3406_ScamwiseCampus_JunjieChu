package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onScenarioClick: (String) -> Unit,
    onPracticeClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onLinkVerificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> {
                HomeMessageContent(
                    message = stringResource(
                        R.string.home_loading_dashboard
                    ),
                    showProgress = true,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.error != null -> {
                HomeMessageContent(
                    message = stringResource(
                        uiState.error.messageRes()
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                HomeDashboard(
                    uiState = uiState,
                    onScenarioClick = onScenarioClick,
                    onPracticeClick = onPracticeClick,
                    onStatisticsClick =
                        onStatisticsClick,
                    onLinkVerificationClick =
                        onLinkVerificationClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}