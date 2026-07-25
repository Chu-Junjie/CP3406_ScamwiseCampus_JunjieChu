package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.Scenario

@Composable
internal fun ScenarioQuestionScreen(
    scenario: Scenario,
    uiState: ScenarioActivityUiState,
    onRiskLevelSelected: (RiskLevel) -> Unit,
    onWarningSignToggled: (String) -> Unit,
    onNoWarningSignsSelected: () -> Unit,
    onActionSelected: (String) -> Unit,
    onConfidenceSelected: (ConfidenceLevel) -> Unit,
    onContinue: () -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            ScenarioHeader(
                title = scenario.title,
                sender = scenario.sender
            )
        }

        item {
            ScenarioProgressHeader(
                currentStep = uiState.currentStep
            )
        }

        item {
            ScenarioMessageCard(
                subject = scenario.subject,
                messageBody = scenario.messageBody
            )
        }

        item {
            ScenarioStepSelection(
                scenario = scenario,
                uiState = uiState,
                onRiskLevelSelected = onRiskLevelSelected,
                onWarningSignToggled = onWarningSignToggled,
                onNoWarningSignsSelected =
                    onNoWarningSignsSelected,
                onActionSelected = onActionSelected,
                onConfidenceSelected =
                    onConfidenceSelected
            )
        }

        uiState.validationError?.let { error ->
            item {
                Text(
                    text = stringResource(
                        error.messageRes()
                    ),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics {
                        liveRegion = LiveRegionMode.Assertive
                    }
                )
            }
        }

        item {
            ScenarioContinueButton(
                currentStep = uiState.currentStep,
                onContinue = onContinue,
                onSubmit = onSubmit
            )
        }
    }
}

@Composable
private fun ScenarioHeader(
    title: String,
    sender: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.semantics {
            heading()
        }
    )

    Text(
        text = sender,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun ScenarioProgressHeader(
    currentStep: ScenarioStep
) {
    LinearProgressIndicator(
        progress = {
            currentStep.progress()
        },
        modifier = Modifier.fillMaxWidth()
    )

    Text(
        text = stringResource(
            currentStep.displayTitleRes()
        ),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
private fun ScenarioMessageCard(
    subject: String?,
    messageBody: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            subject?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Text(
                text = messageBody,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ScenarioContinueButton(
    currentStep: ScenarioStep,
    onContinue: () -> Unit,
    onSubmit: () -> Unit
) {
    val isFinalStep =
        currentStep == ScenarioStep.CONFIDENCE

    Button(
        onClick = {
            if (isFinalStep) {
                onSubmit()
            } else {
                onContinue()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                if (isFinalStep) {
                    R.string.scenario_submit_answer
                } else {
                    R.string.scenario_continue
                }
            )
        )
    }
}
