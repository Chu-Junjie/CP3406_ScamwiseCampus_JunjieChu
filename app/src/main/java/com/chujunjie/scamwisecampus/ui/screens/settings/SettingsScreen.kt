package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onClearHistory: () -> Unit,
    onDismissFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    var showClearHistoryDialog by remember {
        mutableStateOf(false)
    }

    val feedbackMessage =
        uiState.feedback?.let { feedback ->
            stringResource(feedback.messageRes())
        }

    LaunchedEffect(feedbackMessage) {
        if (feedbackMessage != null) {
            snackbarHostState.showSnackbar(
                feedbackMessage
            )
            onDismissFeedback()
        }
    }

    if (showClearHistoryDialog) {
        ClearHistoryDialog(
            isClearing =
                uiState.isClearingHistory,
            onConfirm = {
                showClearHistoryDialog = false
                onClearHistory()
            },
            onDismiss = {
                showClearHistoryDialog = false
            }
        )
    }

    ResponsiveContent(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SnackbarHost(
                hostState = snackbarHostState
            )

            if (uiState.isLoading) {
                LoadingSettingsContent(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                SettingsContent(
                    uiState = uiState,
                    onThemeModeSelected =
                        onThemeModeSelected,
                    onClearHistoryClick = {
                        showClearHistoryDialog = true
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}