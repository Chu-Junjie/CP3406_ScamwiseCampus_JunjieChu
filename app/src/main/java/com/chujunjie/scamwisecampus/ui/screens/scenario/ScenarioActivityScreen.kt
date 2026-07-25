package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.WarningSign
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
                MessageContent(
                    message = stringResource(
                        R.string.scenario_loading
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }

            uiState.isScenarioMissing || scenario == null -> {
                MessageContent(
                    message = stringResource(
                        R.string.scenario_not_found
                    ),
                    modifier = Modifier.fillMaxSize(),
                    actionText = stringResource(
                        R.string.scenario_return_to_practice
                    ),
                    onAction = onReturnToPractice
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
                        requireNotNull(uiState.selectedConfidenceLevel),
                    evaluation = uiState.evaluation,
                    onRestart = onRestart,
                    onReturnHome = onReturnHome,
                    onViewStatistics = onViewStatistics,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 12.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TextButton(
                            onClick = onBack
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.scenario_back
                                )
                            )
                        }
                    }

                    item {
                        Text(
                            text = scenario.title,
                            style =
                                MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.semantics {
                                heading()
                            }
                        )

                        Text(
                            text = scenario.sender,
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    item {
                        LinearProgressIndicator(
                            progress = {
                                uiState.currentStep.progress()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = stringResource(
                                uiState.currentStep.displayTitleRes()
                            ),
                            style =
                                MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                scenario.subject?.let { subject ->
                                    Text(
                                        text = subject,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleSmall
                                    )
                                }

                                Text(
                                    text = scenario.messageBody,
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyLarge,
                                    modifier =
                                        Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    when (uiState.currentStep) {
                        ScenarioStep.RISK_ASSESSMENT -> {
                            item {
                                RiskSelectionContent(
                                    selectedRiskLevel =
                                        uiState.selectedRiskLevel,
                                    onRiskLevelSelected =
                                        onRiskLevelSelected
                                )
                            }
                        }

                        ScenarioStep.WARNING_SIGNS -> {
                            item {
                                WarningSignSelectionContent(
                                    warningSigns =
                                        scenario.warningSigns,
                                    selectedWarningSignIds =
                                        uiState.selectedWarningSignIds,
                                    hasSelectedNoWarningSigns =
                                        uiState
                                            .hasSelectedNoWarningSigns,
                                    onWarningSignToggled =
                                        onWarningSignToggled,
                                    onNoWarningSignsSelected =
                                        onNoWarningSignsSelected
                                )
                            }
                        }

                        ScenarioStep.SAFE_ACTION -> {
                            item {
                                ActionSelectionContent(
                                    actionOptions =
                                        scenario.actionOptions,
                                    selectedActionId =
                                        uiState.selectedActionId,
                                    onActionSelected =
                                        onActionSelected
                                )
                            }
                        }

                        ScenarioStep.CONFIDENCE -> {
                            item {
                                ConfidenceSelectionContent(
                                    selectedConfidence =
                                        uiState
                                            .selectedConfidenceLevel,
                                    onConfidenceSelected =
                                        onConfidenceSelected
                                )
                            }
                        }
                    }

                    uiState.validationMessage?.let { message ->
                        item {
                            Text(
                                text = message,
                                color =
                                    MaterialTheme.colorScheme.error,
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,
                                modifier = Modifier.semantics {
                                    liveRegion =
                                        LiveRegionMode.Assertive
                                }
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (
                                    uiState.currentStep ==
                                    ScenarioStep.CONFIDENCE
                                ) {
                                    onSubmit()
                                } else {
                                    onContinue()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(
                                    if (
                                        uiState.currentStep ==
                                        ScenarioStep.CONFIDENCE
                                    ) {
                                        R.string
                                            .scenario_submit_answer
                                    } else {
                                        R.string.scenario_continue
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RiskSelectionContent(
    selectedRiskLevel: RiskLevel?,
    onRiskLevelSelected: (RiskLevel) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.scenario_risk_question
            ),
            style = MaterialTheme.typography.titleMedium
        )

        RiskLevel.entries.forEach { riskLevel ->
            SelectionRow(
                label = stringResource(
                    riskLevel.displayNameRes()
                ),
                selected = selectedRiskLevel == riskLevel,
                onClick = {
                    onRiskLevelSelected(riskLevel)
                }
            )
        }
    }
}

@Composable
private fun WarningSignSelectionContent(
    warningSigns: List<WarningSign>,
    selectedWarningSignIds: Set<String>,
    hasSelectedNoWarningSigns: Boolean,
    onWarningSignToggled: (String) -> Unit,
    onNoWarningSignsSelected: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.scenario_warning_signs_question
            ),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(
                R.string.scenario_select_all_that_apply
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        warningSigns.forEach { warningSign ->
            CheckboxRow(
                label = warningSign.description,
                checked =
                    warningSign.id in selectedWarningSignIds,
                onClick = {
                    onWarningSignToggled(warningSign.id)
                }
            )
        }

        CheckboxRow(
            label = stringResource(
                R.string.scenario_no_clear_warning_signs
            ),
            checked = hasSelectedNoWarningSigns,
            onClick = onNoWarningSignsSelected
        )
    }
}

@Composable
private fun ActionSelectionContent(
    actionOptions: List<ActionOption>,
    selectedActionId: String?,
    onActionSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.scenario_safe_action_question
            ),
            style = MaterialTheme.typography.titleMedium
        )

        actionOptions.forEach { action ->
            SelectionRow(
                label = action.description,
                selected = selectedActionId == action.id,
                onClick = {
                    onActionSelected(action.id)
                }
            )
        }
    }
}

@Composable
private fun ConfidenceSelectionContent(
    selectedConfidence: ConfidenceLevel?,
    onConfidenceSelected: (ConfidenceLevel) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.scenario_confidence_question
            ),
            style = MaterialTheme.typography.titleMedium
        )

        ConfidenceLevel.entries.forEach { confidenceLevel ->
            SelectionRow(
                label = stringResource(
                    confidenceLevel.displayNameRes()
                ),
                selected =
                    selectedConfidence == confidenceLevel,
                onClick = {
                    onConfidenceSelected(confidenceLevel)
                }
            )
        }
    }
}

@Composable
private fun SelectionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Merge the radio button state and label.
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null
            )

            Text(
                text = label,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Merge the checkbox state and label.
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )

            Text(
                text = label,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun MessageContent(
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

private fun ScenarioStep.progress(): Float {
    return when (this) {
        ScenarioStep.RISK_ASSESSMENT -> 0.25f
        ScenarioStep.WARNING_SIGNS -> 0.50f
        ScenarioStep.SAFE_ACTION -> 0.75f
        ScenarioStep.CONFIDENCE -> 1.00f
    }
}

@StringRes
private fun ScenarioStep.displayTitleRes(): Int {
    return when (this) {
        ScenarioStep.RISK_ASSESSMENT ->
            R.string.scenario_step_assess_risk

        ScenarioStep.WARNING_SIGNS ->
            R.string.scenario_step_find_warning_signs

        ScenarioStep.SAFE_ACTION ->
            R.string.scenario_step_choose_action

        ScenarioStep.CONFIDENCE ->
            R.string.scenario_step_rate_confidence
    }
}

@StringRes
private fun RiskLevel.displayNameRes(): Int {
    return when (this) {
        RiskLevel.HIGH_RISK ->
            R.string.risk_level_high

        RiskLevel.NEEDS_VERIFICATION ->
            R.string.risk_level_needs_verification

        RiskLevel.NO_CLEAR_THREAT ->
            R.string.risk_level_no_clear_threat
    }
}

@StringRes
private fun ConfidenceLevel.displayNameRes(): Int {
    return when (this) {
        ConfidenceLevel.NOT_CONFIDENT ->
            R.string.confidence_not_confident

        ConfidenceLevel.SOMEWHAT_CONFIDENT ->
            R.string.confidence_somewhat_confident

        ConfidenceLevel.VERY_CONFIDENT ->
            R.string.confidence_very_confident
    }
}