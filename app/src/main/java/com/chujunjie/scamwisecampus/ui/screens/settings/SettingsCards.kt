package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ThemeMode

@Composable
internal fun AppearanceSettingsCard(
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit
) {
    SettingsCard(
        title = stringResource(
            R.string.settings_appearance
        )
    ) {
        Text(
            text = stringResource(
                R.string.settings_theme
            ),
            style =
                MaterialTheme.typography.titleSmall
        )

        Text(
            text = stringResource(
                R.string.settings_theme_description
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        ThemeMode.entries.forEach { themeMode ->
            ThemeOptionRow(
                themeMode = themeMode,
                selected =
                    selectedThemeMode == themeMode,
                onClick = {
                    onThemeModeSelected(themeMode)
                },
                modifier =
                    Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
internal fun LocalLearningDataCard(
    isClearingHistory: Boolean,
    onClearHistoryClick: () -> Unit
) {
    SettingsCard(
        title = stringResource(
            R.string.settings_local_learning_data
        )
    ) {
        Text(
            text = stringResource(
                R.string
                    .settings_local_data_description
            ),
            style =
                MaterialTheme.typography.bodyMedium
        )

        OutlinedButton(
            onClick = onClearHistoryClick,
            enabled = !isClearingHistory,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (isClearingHistory) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )

                Text(
                    text = stringResource(
                        R.string.settings_clearing
                    ),
                    modifier =
                        Modifier.padding(start = 8.dp)
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

@Composable
internal fun PrivacySettingsCard() {
    SettingsCard(
        title = stringResource(
            R.string.settings_privacy
        )
    ) {
        Text(
            text = stringResource(
                R.string.settings_privacy_permissions
            ),
            style =
                MaterialTheme.typography.bodyMedium
        )

        Text(
            text = stringResource(
                R.string.settings_privacy_storage
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
internal fun AboutSettingsCard() {
    SettingsCard(
        title = stringResource(
            R.string.settings_about
        )
    ) {
        Text(
            text = stringResource(
                R.string.app_name
            ),
            style =
                MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(
                R.string.home_tagline
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = stringResource(
                R.string.settings_about_description
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
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
                // Merge theme state and description.
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
                modifier =
                    Modifier.padding(start = 8.dp)
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
                        MaterialTheme.colorScheme
                            .onSurfaceVariant
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
                style =
                    MaterialTheme.typography.titleMedium
            )

            Column(
                modifier =
                    Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}