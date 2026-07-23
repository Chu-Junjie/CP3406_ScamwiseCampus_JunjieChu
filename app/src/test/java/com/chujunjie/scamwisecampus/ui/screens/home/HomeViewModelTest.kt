package com.chujunjie.scamwisecampus.ui.screens.home

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scenarios =
        ScenarioSeedData.scenarios

    @Test
    fun `empty history recommends an easy first scenario`() {
        val viewModel = createViewModel(
            attempts = emptyList()
        )

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(0, state.totalAttempts)
        assertEquals(18, state.totalScenarioCount)
        assertEquals(0, state.completionPercent)
        assertNotNull(state.recommendedScenario)
        assertEquals(
            Difficulty.EASY,
            state.recommendedScenario?.difficulty
        )
    }

    @Test
    fun `dashboard calculates progress and latest result`() {
        val attempts = listOf(
            createAttempt(
                attemptId = 1,
                scenarioId = "job_easy_01",
                category = ScamCategory.JOB,
                totalScore = 100,
                completedAt = 1_000
            ),
            createAttempt(
                attemptId = 2,
                scenarioId = "banking_easy_01",
                category = ScamCategory.BANKING,
                totalScore = 60,
                completedAt = 2_000
            )
        )

        val state = createViewModel(attempts)
            .uiState.value

        assertEquals(2, state.totalAttempts)
        assertEquals(2, state.completedScenarioCount)
        assertEquals(80, state.averageScore)
        assertEquals(
            2L,
            state.latestAttempt?.attemptId
        )
        assertEquals(11, state.completionPercent)
    }

    @Test
    fun `lowest scoring category is recommended`() {
        val attempts = listOf(
            createAttempt(
                attemptId = 1,
                scenarioId = "job_easy_01",
                category = ScamCategory.JOB,
                totalScore = 90,
                completedAt = 1_000
            ),
            createAttempt(
                attemptId = 2,
                scenarioId = "banking_easy_01",
                category = ScamCategory.BANKING,
                totalScore = 50,
                completedAt = 2_000
            )
        )

        val state = createViewModel(attempts)
            .uiState.value

        assertEquals(
            ScamCategory.BANKING,
            state.recommendedCategory
        )

        assertEquals(
            ScamCategory.BANKING,
            state.recommendedScenario?.category
        )
    }

    private fun createViewModel(
        attempts: List<AttemptRecord>
    ): HomeViewModel {
        return HomeViewModel(
            attemptRepository =
                FakeAttemptRepository(attempts),
            scenarioRepository =
                FakeScenarioRepository(scenarios)
        )
    }

    private fun createAttempt(
        attemptId: Long,
        scenarioId: String,
        category: ScamCategory,
        totalScore: Int,
        completedAt: Long
    ): AttemptRecord {
        return AttemptRecord(
            attemptId = attemptId,
            scenarioId = scenarioId,
            category = category,
            difficulty = Difficulty.EASY,
            selectedRiskLevel =
                RiskLevel.HIGH_RISK,
            correctRiskLevel =
                RiskLevel.HIGH_RISK,
            selectedWarningSignIds =
                emptySet(),
            selectedNoWarningSigns = false,
            selectedActionId =
                "action_$attemptId",
            confidenceLevel =
                ConfidenceLevel.SOMEWHAT_CONFIDENT,
            riskScore = 40,
            warningSignScore = 30,
            safeActionScore =
                (totalScore - 70).coerceAtLeast(0),
            totalScore = totalScore,
            isRiskCorrect = true,
            isSafeActionCorrect =
                totalScore >= 70,
            confidenceCalibration =
                ConfidenceCalibration.WELL_CALIBRATED,
            completedAtEpochMillis = completedAt
        )
    }

    private class FakeAttemptRepository(
        initialAttempts: List<AttemptRecord>
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
            return 1
        }

        override suspend fun clearAttempts() {
            attempts.value = emptyList()
        }
    }

    private class FakeScenarioRepository(
        private val scenarios: List<Scenario>
    ) : ScenarioRepository {

        override fun observeScenarios():
            Flow<List<Scenario>> {
            return MutableStateFlow(scenarios)
        }

        override fun getScenarioById(
            scenarioId: String
        ): Scenario? {
            return scenarios.firstOrNull { scenario ->
                scenario.id == scenarioId
            }
        }
    }
}
