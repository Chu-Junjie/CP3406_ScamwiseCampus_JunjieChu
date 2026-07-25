package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScenarioActivityRoute(
    scenarioId: String,
    onNavigateBack: () -> Unit,
    onReturnToPractice: () -> Unit,
    onReturnHome: () -> Unit,
    onViewStatistics: () -> Unit,
    viewModel: ScenarioActivityViewModel = koinViewModel(
        parameters = {
            parametersOf(scenarioId)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val handleBack: () -> Unit = {
        val movedWithinScenario =
            uiState.evaluation == null &&
                    !uiState.isLoading &&
                    !uiState.isScenarioMissing &&
                    uiState.scenario != null &&
                    viewModel.moveToPreviousStep()

        if (!movedWithinScenario) {
            onNavigateBack()
        }
    }

    BackHandler(
        onBack = handleBack
    )

    ScenarioActivityScreen(
        uiState = uiState,
        onRiskLevelSelected = viewModel::selectRiskLevel,
        onWarningSignToggled = viewModel::toggleWarningSign,
        onNoWarningSignsSelected =
            viewModel::selectNoWarningSigns,
        onActionSelected = viewModel::selectAction,
        onConfidenceSelected =
            viewModel::selectConfidence,
        onContinue = viewModel::continueToNextStep,
        onSubmit = viewModel::submitAttempt,
        onRestart = viewModel::restartScenario,
        onBack = handleBack,
        onReturnToPractice = onReturnToPractice,
        onReturnHome = onReturnHome,
        onViewStatistics = onViewStatistics
    )
}