package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
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
                text = stringResource(
                    R.string.settings_title
                ),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Text(
                text = stringResource(
                    R.string.settings_description
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            SettingsCard(
                title = stringResource(
                    R.string.settings_appearance
                )
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_theme
                    ),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = stringResource(
                        R.string.settings_theme_description
                    ),
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
                title = stringResource(
                    R.string.settings_local_learning_data
                )
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_local_data_description
                    ),
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
                            text = stringResource(
                                R.string.settings_clearing
                            ),
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(
                                R.string
                                    .settings_clear_practice_history
                            )
                        )
                    }
                }
            }
        }

        item {
            SettingsCard(
                title = stringResource(
                    R.string.settings_privacy
                )
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_privacy_permissions
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(
                        R.string.settings_privacy_storage
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        item {
            SettingsCard(
                title = stringResource(
                    R.string.settings_about
                )
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        R.string.home_tagline
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = stringResource(
                        R.string.settings_about_description
                    ),
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
                    text = stringResource(
                        themeMode.displayNameRes()
                    ),
                    style =
                        MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = stringResource(
                        themeMode.descriptionRes()
                    ),
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
            Text(
                text = stringResource(
                    R.string.settings_clear_history_dialog_title
                )
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.settings_clear_history_dialog_message
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isClearing
            ) {
                Text(
                    text = stringResource(
                        R.string.settings_clear_history_confirm
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isClearing
            ) {
                Text(
                    text = stringResource(
                        R.string.common_cancel
                    )
                )
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
            liveRegion = LiveRegionMode.Polite
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = stringResource(
                R.string.settings_loading
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@StringRes
private fun ThemeMode.displayNameRes(): Int {
    return when (this) {
        ThemeMode.SYSTEM ->
            R.string.theme_mode_system

        ThemeMode.LIGHT ->
            R.string.theme_mode_light

        ThemeMode.DARK ->
            R.string.theme_mode_dark
    }
}

@StringRes
private fun ThemeMode.descriptionRes(): Int {
    return when (this) {
        ThemeMode.SYSTEM ->
            R.string.theme_mode_system_description

        ThemeMode.LIGHT ->
            R.string.theme_mode_light_description

        ThemeMode.DARK ->
            R.string.theme_mode_dark_description
    }
}