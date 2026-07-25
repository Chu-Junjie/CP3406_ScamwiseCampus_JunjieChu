package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

@Composable
internal fun ScenarioStepSelection(
    scenario: Scenario,
    uiState: ScenarioActivityUiState,
    onRiskLevelSelected: (RiskLevel) -> Unit,
    onWarningSignToggled: (String) -> Unit,
    onNoWarningSignsSelected: () -> Unit,
    onActionSelected: (String) -> Unit,
    onConfidenceSelected: (ConfidenceLevel) -> Unit
) {
    when (uiState.currentStep) {
        ScenarioStep.RISK_ASSESSMENT -> {
            RiskSelectionContent(
                selectedRiskLevel =
                    uiState.selectedRiskLevel,
                onRiskLevelSelected =
                    onRiskLevelSelected
            )
        }

        ScenarioStep.WARNING_SIGNS -> {
            WarningSignSelectionContent(
                warningSigns = scenario.warningSigns,
                selectedWarningSignIds =
                    uiState.selectedWarningSignIds,
                hasSelectedNoWarningSigns =
                    uiState.hasSelectedNoWarningSigns,
                onWarningSignToggled =
                    onWarningSignToggled,
                onNoWarningSignsSelected =
                    onNoWarningSignsSelected
            )
        }

        ScenarioStep.SAFE_ACTION -> {
            ActionSelectionContent(
                actionOptions = scenario.actionOptions,
                selectedActionId =
                    uiState.selectedActionId,
                onActionSelected =
                    onActionSelected
            )
        }

        ScenarioStep.CONFIDENCE -> {
            ConfidenceSelectionContent(
                selectedConfidence =
                    uiState.selectedConfidenceLevel,
                onConfidenceSelected =
                    onConfidenceSelected
            )
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
