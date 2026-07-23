package com.chujunjie.scamwisecampus.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val attemptRepository: AttemptRepository,
    private val scenarioRepository: ScenarioRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        observeDashboard()
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            combine(
                attemptRepository.observeAttempts(),
                scenarioRepository.observeScenarios()
            ) { attempts, scenarios ->
                createHomeUiState(
                    attempts = attempts,
                    scenarios = scenarios
                )
            }
                .catch {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        errorMessage =
                            "Home information could not be loaded."
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun createHomeUiState(
        attempts: List<AttemptRecord>,
        scenarios: List<Scenario>
    ): HomeUiState {
        val sortedAttempts =
            attempts.sortedByDescending { attempt ->
                attempt.completedAtEpochMillis
            }

        val recommendedCategory =
            findRecommendedCategory(attempts)

        val recommendedScenario =
            findRecommendedScenario(
                attempts = attempts,
                scenarios = scenarios,
                recommendedCategory = recommendedCategory
            )

        return HomeUiState(
            totalAttempts = attempts.size,
            completedScenarioCount = attempts
                .map { attempt -> attempt.scenarioId }
                .distinct()
                .size,
            totalScenarioCount = scenarios.size,
            averageScore = if (attempts.isEmpty()) {
                0
            } else {
                attempts
                    .map { attempt -> attempt.totalScore }
                    .average()
                    .roundToInt()
            },
            latestAttempt = sortedAttempts.firstOrNull(),
            recommendedCategory =
                recommendedScenario?.category,
            recommendedScenario = recommendedScenario,
            isLoading = false
        )
    }

    private fun findRecommendedCategory(
        attempts: List<AttemptRecord>
    ): ScamCategory? {
        if (attempts.isEmpty()) {
            return null
        }

        return attempts
            .groupBy { attempt -> attempt.category }
            .minWithOrNull(
                compareBy<Map.Entry<ScamCategory, List<AttemptRecord>>> {
                    entry ->
                        entry.value
                            .map { attempt -> attempt.totalScore }
                            .average()
                }.thenBy { entry ->
                    entry.value.size
                }
            )
            ?.key
    }

    private fun findRecommendedScenario(
        attempts: List<AttemptRecord>,
        scenarios: List<Scenario>,
        recommendedCategory: ScamCategory?
    ): Scenario? {
        if (scenarios.isEmpty()) {
            return null
        }

        if (attempts.isEmpty()) {
            return scenarios.firstOrNull { scenario ->
                scenario.difficulty == Difficulty.EASY
            } ?: scenarios.first()
        }

        val candidateScenarios = scenarios.filter { scenario ->
            scenario.category == recommendedCategory
        }

        val attemptCounts = attempts
            .groupingBy { attempt -> attempt.scenarioId }
            .eachCount()

        return candidateScenarios.minWithOrNull(
            compareBy<Scenario> { scenario ->
                attemptCounts[scenario.id] ?: 0
            }.thenBy { scenario ->
                scenario.difficulty.ordinal
            }
        ) ?: scenarios.first()
    }
}
