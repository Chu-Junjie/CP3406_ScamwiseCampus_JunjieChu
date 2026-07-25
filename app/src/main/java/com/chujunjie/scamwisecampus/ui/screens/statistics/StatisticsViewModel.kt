package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val attemptRepository: AttemptRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(StatisticsUiState())

    val uiState: StateFlow<StatisticsUiState> =
        _uiState.asStateFlow()

    init {
        observeAttemptHistory()
    }

    private fun observeAttemptHistory() {
        viewModelScope.launch {
            attemptRepository.observeAttempts()
                .catch {
                    _uiState.value = StatisticsUiState(
                        isLoading = false,
                        error =
                            StatisticsStatusMessage.LOAD_FAILED
                    )
                }
                .collect { attempts ->
                    _uiState.value =
                        attempts.toStatisticsUiState()
                }
        }
    }
}

private fun List<AttemptRecord>.toStatisticsUiState(): StatisticsUiState {
    if (isEmpty()) {
        return StatisticsUiState(
            isLoading = false
        )
    }

    val sortedAttempts =
        sortedByDescending { attempt ->
            attempt.completedAtEpochMillis
        }

    val categoryPerformance =
        groupBy { attempt -> attempt.category }
            .map { (category, categoryAttempts) ->
                CategoryPerformance(
                    category = category,
                    attemptCount = categoryAttempts.size,
                    averageScore = categoryAttempts
                        .map { attempt -> attempt.totalScore }
                        .average()
                        .roundToInt(),
                    highestScore = categoryAttempts
                        .maxOf { attempt -> attempt.totalScore },
                    riskAccuracyPercent = calculatePercentage(
                        matchingCount = categoryAttempts.count {
                                attempt -> attempt.isRiskCorrect
                        },
                        totalCount = categoryAttempts.size
                    ),
                    safeActionAccuracyPercent =
                        calculatePercentage(
                            matchingCount =
                                categoryAttempts.count {
                                        attempt ->
                                    attempt.isSafeActionCorrect
                                },
                            totalCount =
                                categoryAttempts.size
                        )
                )
            }
            .sortedBy { performance ->
                performance.category.ordinal
            }

    val recommendedCategory =
        categoryPerformance.minWithOrNull(
            compareBy<CategoryPerformance> {
                    performance -> performance.averageScore
            }.thenBy {
                    performance -> performance.attemptCount
            }
        )?.category

    return StatisticsUiState(
        totalAttempts = size,
        averageScore = map { attempt -> attempt.totalScore }
            .average()
            .roundToInt(),
        highestScore = maxOf { attempt -> attempt.totalScore },
        riskAccuracyPercent = calculatePercentage(
            matchingCount = count { attempt ->
                attempt.isRiskCorrect
            },
            totalCount = size
        ),
        safeActionAccuracyPercent = calculatePercentage(
            matchingCount = count { attempt ->
                attempt.isSafeActionCorrect
            },
            totalCount = size
        ),
        wellCalibratedCount = countCalibration(
            ConfidenceCalibration.WELL_CALIBRATED
        ),
        underconfidentCount = countCalibration(
            ConfidenceCalibration.UNDERCONFIDENT
        ),
        overconfidentCount = countCalibration(
            ConfidenceCalibration.OVERCONFIDENT
        ),
        needsReviewCount = countCalibration(
            ConfidenceCalibration.NEEDS_REVIEW
        ),
        cautiousButIncorrectCount = countCalibration(
            ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT
        ),
        categoryPerformance = categoryPerformance,
        recommendedCategory = recommendedCategory,
        recentAttempts = sortedAttempts.take(
            RECENT_ATTEMPT_LIMIT
        ),
        isLoading = false
    )
}

private fun List<AttemptRecord>.countCalibration(
    calibration: ConfidenceCalibration
): Int {
    return count { attempt ->
        attempt.confidenceCalibration == calibration
    }
}

private fun calculatePercentage(
    matchingCount: Int,
    totalCount: Int
): Int {
    if (totalCount == 0) {
        return 0
    }

    return (
            matchingCount.toDouble() /
                    totalCount *
                    PERCENTAGE_MAXIMUM
            )
        .roundToInt()
}

private const val PERCENTAGE_MAXIMUM = 100
private const val RECENT_ATTEMPT_LIMIT = 5
