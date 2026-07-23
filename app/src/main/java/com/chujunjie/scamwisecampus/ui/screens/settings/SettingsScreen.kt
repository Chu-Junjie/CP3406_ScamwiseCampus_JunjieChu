package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.ThemeMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.size
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion

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

    LaunchedEffect(uiState.feedbackMessage) {
        val message = uiState.feedbackMessage

        if (message != null) {
            snackbarHostState.showSnackbar(message)
            onDismissFeedback()
        }
    }

    if (showClearHistoryDialog) {
        ClearHistoryDialog(
            isClearing = uiState.isClearingHistory,
            onConfirm = {
                showClearHistoryDialog = false
                onClearHistory()
            },
            onDismiss = {
                showClearHistoryDialog = false
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
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

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onClearHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 20.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = "Control appearance and locally stored learning data.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            SettingsCard(
                title = "Appearance"
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Choose how ScamWise Campus appears on this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                ThemeMode.entries.forEach { themeMode ->
                    ThemeOptionRow(
                        themeMode = themeMode,
                        selected =
                            uiState.themeMode == themeMode,
                        onClick = {
                            onThemeModeSelected(themeMode)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        item {
            SettingsCard(
                title = "Local Learning Data"
            ) {
                Text(
                    text = "Practice attempts are stored only on this device and are used to calculate Statistics.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedButton(
                    onClick = onClearHistoryClick,
                    enabled =
                        !uiState.isClearingHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    if (uiState.isClearingHistory) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )

                        Text(
                            text = "Clearing...",
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    } else {
                        Text(text = "Clear Practice History")
                    }
                }
            }
        }

        item {
            SettingsCard(
                title = "Privacy"
            ) {
                Text(
                    text = "ScamWise Campus does not require an account and does not read messages, contacts, files, or location.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Practice answers and statistics remain in the app's private local storage unless you clear them.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        item {
            SettingsCard(
                title = "About"
            ) {
                Text(
                    text = "ScamWise Campus",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Pause. Check. Protect.",
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "An educational app for practising safer digital judgement.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    themeMode: ThemeMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Merge the theme selection state and description.
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null
            )

            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = themeMode.displayName(),
                    style =
                        MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = themeMode.description(),
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
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
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun ClearHistoryDialog(
    isClearing: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isClearing) {
                onDismiss()
            }
        },
        title = {
            Text(text = "Clear practice history?")
        },
        text = {
            Text(
                text = "This permanently removes all locally stored attempts and resets Statistics. This action cannot be undone."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isClearing
            ) {
                Text(text = "Clear History")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isClearing
            ) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun LoadingSettingsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.semantics {
            liveRegion =
                LiveRegionMode.Polite
        },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = "Loading settings...",
            modifier = Modifier.padding(
                top = 16.dp
            )
        )
    }
}

private fun ThemeMode.displayName(): String {
    return when (this) {
        ThemeMode.SYSTEM -> "Use System Setting"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }
}

private fun ThemeMode.description(): String {
    return when (this) {
        ThemeMode.SYSTEM ->
            "Follow the device appearance."

        ThemeMode.LIGHT ->
            "Always use the light theme."

        ThemeMode.DARK ->
            "Always use the dark theme."
    }
}
