package com.chujunjie.scamwisecampus.ui.screens.statistics

import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class StatisticsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `empty history displays empty statistics state`() {
        val repository = FakeAttemptRepository()
        val viewModel = StatisticsViewModel(repository)

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.hasAttempts)
        assertEquals(0, state.totalAttempts)
        assertNull(state.recommendedCategory)
    }

    @Test
    fun `attempt history calculates overall statistics`() {
        val repository = FakeAttemptRepository(
            initialAttempts = sampleAttempts()
        )

        val state = StatisticsViewModel(repository)
            .uiState.value

        assertEquals(3, state.totalAttempts)
        assertEquals(77, state.averageScore)
        assertEquals(100, state.highestScore)
        assertEquals(67, state.riskAccuracyPercent)
        assertEquals(67, state.safeActionAccuracyPercent)
        assertEquals(1, state.wellCalibratedCount)
        assertEquals(1, state.underconfidentCount)
        assertEquals(1, state.overconfidentCount)
    }

    @Test
    fun `attempt history calculates category performance`() {
        val repository = FakeAttemptRepository(
            initialAttempts = sampleAttempts()
        )

        val state = StatisticsViewModel(repository)
            .uiState.value

        val jobPerformance =
            state.categoryPerformance.first {
                performance ->
                    performance.category ==
                        ScamCategory.JOB
            }

        val bankingPerformance =
            state.categoryPerformance.first {
                performance ->
                    performance.category ==
                        ScamCategory.BANKING
            }

        assertEquals(2, jobPerformance.attemptCount)
        assertEquals(75, jobPerformance.averageScore)
        assertEquals(100, jobPerformance.highestScore)
        assertEquals(50, jobPerformance.riskAccuracyPercent)
        assertEquals(
            50,
            jobPerformance.safeActionAccuracyPercent
        )

        assertEquals(1, bankingPerformance.attemptCount)
        assertEquals(80, bankingPerformance.averageScore)
    }

    @Test
    fun `lowest scoring practised category is recommended`() {
        val repository = FakeAttemptRepository(
            initialAttempts = sampleAttempts()
        )

        val state = StatisticsViewModel(repository)
            .uiState.value

        assertEquals(
            ScamCategory.JOB,
            state.recommendedCategory
        )
    }

    @Test
    fun `recent attempts are ordered newest first`() {
        val repository = FakeAttemptRepository(
            initialAttempts = sampleAttempts()
        )

        val recentAttempts = StatisticsViewModel(repository)
            .uiState.value
            .recentAttempts

        assertEquals(
            listOf(3L, 2L, 1L),
            recentAttempts.map { attempt ->
                attempt.attemptId
            }
        )
    }

    private fun sampleAttempts(): List<AttemptRecord> {
        return listOf(
            createAttempt(
                attemptId = 1,
                category = ScamCategory.JOB,
                totalScore = 100,
                isRiskCorrect = true,
                isSafeActionCorrect = true,
                calibration =
                    ConfidenceCalibration.WELL_CALIBRATED,
                completedAt = 1_000
            ),
            createAttempt(
                attemptId = 2,
                category = ScamCategory.JOB,
                totalScore = 50,
                isRiskCorrect = false,
                isSafeActionCorrect = false,
                calibration =
                    ConfidenceCalibration.OVERCONFIDENT,
                completedAt = 2_000
            ),
            createAttempt(
                attemptId = 3,
                category = ScamCategory.BANKING,
                totalScore = 80,
                isRiskCorrect = true,
                isSafeActionCorrect = true,
                calibration =
                    ConfidenceCalibration.UNDERCONFIDENT,
                completedAt = 3_000
            )
        )
    }

    private fun createAttempt(
        attemptId: Long,
        category: ScamCategory,
        totalScore: Int,
        isRiskCorrect: Boolean,
        isSafeActionCorrect: Boolean,
        calibration: ConfidenceCalibration,
        completedAt: Long
    ): AttemptRecord {
        return AttemptRecord(
            attemptId = attemptId,
            scenarioId = "scenario_$attemptId",
            category = category,
            difficulty = Difficulty.EASY,
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            correctRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = emptySet(),
            selectedNoWarningSigns = false,
            selectedActionId = "action_$attemptId",
            confidenceLevel =
                ConfidenceLevel.SOMEWHAT_CONFIDENT,
            riskScore = if (isRiskCorrect) 40 else 0,
            warningSignScore = 30,
            safeActionScore =
                if (isSafeActionCorrect) 30 else 0,
            totalScore = totalScore,
            isRiskCorrect = isRiskCorrect,
            isSafeActionCorrect =
                isSafeActionCorrect,
            confidenceCalibration = calibration,
            completedAtEpochMillis = completedAt
        )
    }

    private class FakeAttemptRepository(
        initialAttempts: List<AttemptRecord> =
            emptyList()
    ) : AttemptRepository {

        private val attempts =
            MutableStateFlow(initialAttempts)

        override fun observeAttempts():
            Flow<List<AttemptRecord>> {
            return attempts
        }

        override suspend fun saveAttempt(
            attemptRecord: AttemptRecord
        ): Long {
            val attemptId =
                attempts.value.size.toLong() + 1

            attempts.value =
                attempts.value +
                    attemptRecord.copy(
                        attemptId = attemptId
                    )

            return attemptId
        }

        override suspend fun clearAttempts() {
            attempts.value = emptyList()
        }
    }
}
