package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun ScenarioActivityScreen(
    uiState: ScenarioActivityUiState,
    onRiskLevelSelected: (RiskLevel) -> Unit,
    onWarningSignToggled: (String) -> Unit,
    onNoWarningSignsSelected: () -> Unit,
    onActionSelected: (String) -> Unit,
    onConfidenceSelected: (ConfidenceLevel) -> Unit,
    onContinue: () -> Unit,
    onSubmit: () -> Unit,
    onRestart: () -> Unit,
    onBack: () -> Unit,
    onReturnToPractice: () -> Unit,
    onReturnHome: () -> Unit,
    onViewStatistics: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        val scenario = uiState.scenario

        when {
            uiState.isLoading -> {
                ScenarioMessageContent(
                    message = stringResource(
                        R.string.scenario_loading
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.isScenarioMissing || scenario == null -> {
                ScenarioMessageContent(
                    message = stringResource(
                        R.string.scenario_not_found
                    ),
                    actionText = stringResource(
                        R.string.scenario_return_to_practice
                    ),
                    onAction = onReturnToPractice,
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.evaluation != null -> {
                ScenarioResultScreen(
                    scenario = scenario,
                    selectedRiskLevel =
                        requireNotNull(uiState.selectedRiskLevel),
                    selectedWarningSignIds =
                        uiState.selectedWarningSignIds,
                    hasSelectedNoWarningSigns =
                        uiState.hasSelectedNoWarningSigns,
                    selectedActionId =
                        requireNotNull(uiState.selectedActionId),
                    selectedConfidenceLevel =
                        requireNotNull(
                            uiState.selectedConfidenceLevel
                        ),
                    evaluation = uiState.evaluation,
                    onRestart = onRestart,
                    onReturnHome = onReturnHome,
                    onViewStatistics = onViewStatistics,
                    saveStatus = uiState.saveStatus,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                ScenarioQuestionScreen(
                    scenario = scenario,
                    uiState = uiState,
                    onRiskLevelSelected = onRiskLevelSelected,
                    onWarningSignToggled = onWarningSignToggled,
                    onNoWarningSignsSelected =
                        onNoWarningSignsSelected,
                    onActionSelected = onActionSelected,
                    onConfidenceSelected =
                        onConfidenceSelected,
                    onContinue = onContinue,
                    onSubmit = onSubmit,
                    onBack = onBack,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ScenarioMessageContent(
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )

        if (
            actionText != null &&
            onAction != null
        ) {
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = actionText)
            }
        }
    }
}
