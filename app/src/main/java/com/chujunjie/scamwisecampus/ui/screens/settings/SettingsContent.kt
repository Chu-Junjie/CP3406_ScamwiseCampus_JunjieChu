package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ThemeMode

@Composable
internal fun SettingsContent(
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
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        item {
            SettingsHeader()
        }

        item {
            AppearanceSettingsCard(
                selectedThemeMode =
                    uiState.themeMode,
                onThemeModeSelected =
                    onThemeModeSelected
            )
        }

        item {
            LocalLearningDataCard(
                isClearingHistory =
                    uiState.isClearingHistory,
                onClearHistoryClick =
                    onClearHistoryClick
            )
        }

        item {
            PrivacySettingsCard()
        }

        item {
            AboutSettingsCard()
        }
    }
}

@Composable
private fun SettingsHeader() {
    Text(
        text = stringResource(
            R.string.settings_title
        ),
        style =
            MaterialTheme.typography.headlineMedium,
        modifier = Modifier.semantics {
            heading()
        }
    )

    Text(
        text = stringResource(
            R.string.settings_description
        ),
        style =
            MaterialTheme.typography.bodyMedium,
        color =
            MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}