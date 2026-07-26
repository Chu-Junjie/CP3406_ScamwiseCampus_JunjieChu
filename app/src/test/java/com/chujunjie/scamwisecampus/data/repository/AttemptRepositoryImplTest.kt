package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.local.database.dao.AttemptDao
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWarningSelectionEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWithSelections
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AttemptRepositoryImplTest {

    @Test
    fun `observe attempts maps database values to domain values`() =
        runTest {
            val dao = FakeAttemptDao()
            val repository = AttemptRepositoryImpl(dao)

            dao.emitAttempts(
                listOf(
                    createStoredAttempt()
                )
            )

            val result =
                repository.observeAttempts().first()

            assertEquals(1, result.size)

            val attempt = result.single()

            assertEquals(42L, attempt.attemptId)
            assertEquals(
                "banking_medium_01",
                attempt.scenarioId
            )
            assertEquals(
                ScamCategory.BANKING,
                attempt.category
            )
            assertEquals(
                Difficulty.MEDIUM,
                attempt.difficulty
            )
            assertEquals(
                RiskLevel.NEEDS_VERIFICATION,
                attempt.selectedRiskLevel
            )
            assertEquals(
                RiskLevel.HIGH_RISK,
                attempt.correctRiskLevel
            )
            assertEquals(
                setOf("urgent", "unknown_link"),
                attempt.selectedWarningSignIds
            )
            assertEquals(
                false,
                attempt.selectedNoWarningSigns
            )
            assertEquals(
                "verify_official_channel",
                attempt.selectedActionId
            )
            assertEquals(
                ConfidenceLevel.SOMEWHAT_CONFIDENT,
                attempt.confidenceLevel
            )
            assertEquals(20, attempt.riskScore)
            assertEquals(25, attempt.warningSignScore)
            assertEquals(30, attempt.safeActionScore)
            assertEquals(75, attempt.totalScore)
            assertEquals(false, attempt.isRiskCorrect)
            assertEquals(
                true,
                attempt.isSafeActionCorrect
            )
            assertEquals(
                ConfidenceCalibration.UNDERCONFIDENT,
                attempt.confidenceCalibration
            )
            assertEquals(
                1_234_567L,
                attempt.completedAtEpochMillis
            )
        }

    @Test
    fun `observe attempts removes duplicate warning sign ids`() =
        runTest {
            val dao = FakeAttemptDao()
            val repository = AttemptRepositoryImpl(dao)

            val storedAttempt = createStoredAttempt(
                warningSignIds = listOf(
                    "urgent",
                    "urgent",
                    "unknown_link"
                )
            )

            dao.emitAttempts(listOf(storedAttempt))

            val result =
                repository.observeAttempts()
                    .first()
                    .single()

            assertEquals(
                setOf("urgent", "unknown_link"),
                result.selectedWarningSignIds
            )
        }

    @Test
    fun `save attempt maps every domain value to the dao`() =
        runTest {
            val dao = FakeAttemptDao(
                insertResult = 81L
            )
            val repository = AttemptRepositoryImpl(dao)
            val attempt = createDomainAttempt()

            val insertedId =
                repository.saveAttempt(attempt)

            assertEquals(81L, insertedId)

            val entity =
                requireNotNull(dao.savedAttemptEntity)

            assertEquals(0L, entity.attemptId)
            assertEquals(
                attempt.scenarioId,
                entity.scenarioId
            )
            assertEquals(
                attempt.category.name,
                entity.category
            )
            assertEquals(
                attempt.difficulty.name,
                entity.difficulty
            )
            assertEquals(
                attempt.selectedRiskLevel.name,
                entity.selectedRiskLevel
            )
            assertEquals(
                attempt.correctRiskLevel.name,
                entity.correctRiskLevel
            )
            assertEquals(
                attempt.selectedNoWarningSigns,
                entity.selectedNoWarningSigns
            )
            assertEquals(
                attempt.selectedActionId,
                entity.selectedActionId
            )
            assertEquals(
                attempt.confidenceLevel.name,
                entity.confidenceLevel
            )
            assertEquals(
                attempt.riskScore,
                entity.riskScore
            )
            assertEquals(
                attempt.warningSignScore,
                entity.warningSignScore
            )
            assertEquals(
                attempt.safeActionScore,
                entity.safeActionScore
            )
            assertEquals(
                attempt.totalScore,
                entity.totalScore
            )
            assertEquals(
                attempt.isRiskCorrect,
                entity.isRiskCorrect
            )
            assertEquals(
                attempt.isSafeActionCorrect,
                entity.isSafeActionCorrect
            )
            assertEquals(
                attempt.confidenceCalibration.name,
                entity.confidenceCalibration
            )
            assertEquals(
                attempt.completedAtEpochMillis,
                entity.completedAtEpochMillis
            )
            assertEquals(
                attempt.selectedWarningSignIds,
                dao.savedWarningSignIds
            )
        }

    @Test
    fun `clear attempts delegates to the dao`() =
        runTest {
            val dao = FakeAttemptDao()
            val repository = AttemptRepositoryImpl(dao)

            repository.clearAttempts()

            assertEquals(1, dao.clearCallCount)
            assertTrue(
                dao.observeAttemptsWithSelections()
                    .first()
                    .isEmpty()
            )
        }

    private fun createDomainAttempt(): AttemptRecord {
        return AttemptRecord(
            attemptId = 0,
            scenarioId = "parcel_hard_02",
            category = ScamCategory.PARCEL,
            difficulty = Difficulty.HARD,
            selectedRiskLevel =
                RiskLevel.HIGH_RISK,
            correctRiskLevel =
                RiskLevel.HIGH_RISK,
            selectedWarningSignIds =
                setOf(
                    "payment_request",
                    "shortened_link"
                ),
            selectedNoWarningSigns = false,
            selectedActionId =
                "open_official_app",
            confidenceLevel =
                ConfidenceLevel.VERY_CONFIDENT,
            riskScore = 40,
            warningSignScore = 30,
            safeActionScore = 30,
            totalScore = 100,
            isRiskCorrect = true,
            isSafeActionCorrect = true,
            confidenceCalibration =
                ConfidenceCalibration.WELL_CALIBRATED,
            completedAtEpochMillis = 9_876_543L
        )
    }

    private fun createStoredAttempt(
        warningSignIds: List<String> =
            listOf("urgent", "unknown_link")
    ): AttemptWithSelections {
        val entity = AttemptEntity(
            attemptId = 42L,
            scenarioId = "banking_medium_01",
            category = ScamCategory.BANKING.name,
            difficulty = Difficulty.MEDIUM.name,
            selectedRiskLevel =
                RiskLevel.NEEDS_VERIFICATION.name,
            correctRiskLevel =
                RiskLevel.HIGH_RISK.name,
            selectedNoWarningSigns = false,
            selectedActionId =
                "verify_official_channel",
            confidenceLevel =
                ConfidenceLevel
                    .SOMEWHAT_CONFIDENT
                    .name,
            riskScore = 20,
            warningSignScore = 25,
            safeActionScore = 30,
            totalScore = 75,
            isRiskCorrect = false,
            isSafeActionCorrect = true,
            confidenceCalibration =
                ConfidenceCalibration
                    .UNDERCONFIDENT
                    .name,
            completedAtEpochMillis = 1_234_567L
        )

        return AttemptWithSelections(
            attempt = entity,
            warningSelections =
                warningSignIds.map { warningSignId ->
                    AttemptWarningSelectionEntity(
                        attemptId = entity.attemptId,
                        warningSignId = warningSignId
                    )
                }
        )
    }

    private class FakeAttemptDao(
        private val insertResult: Long = 1L
    ) : AttemptDao() {

        private val storedAttempts =
            MutableStateFlow<
                List<AttemptWithSelections>
            >(emptyList())

        var savedAttemptEntity: AttemptEntity? = null
            private set

        var savedWarningSignIds: Set<String>? = null
            private set

        var clearCallCount: Int = 0
            private set

        fun emitAttempts(
            attempts: List<AttemptWithSelections>
        ) {
            storedAttempts.value = attempts
        }

        override suspend fun insertAttemptEntity(
            attemptEntity: AttemptEntity
        ): Long {
            savedAttemptEntity = attemptEntity
            return insertResult
        }

        override suspend fun insertWarningSelectionEntities(
            warningSelections:
                List<AttemptWarningSelectionEntity>
        ) {
            savedWarningSignIds =
                warningSelections
                    .map { selection ->
                        selection.warningSignId
                    }
                    .toSet()
        }

        override suspend fun insertAttemptWithSelections(
            attemptEntity: AttemptEntity,
            selectedWarningSignIds: Set<String>
        ): Long {
            savedAttemptEntity = attemptEntity
            savedWarningSignIds =
                selectedWarningSignIds
            return insertResult
        }

        override fun observeAttemptsWithSelections():
            Flow<List<AttemptWithSelections>> {
            return storedAttempts
        }

        override suspend fun clearAttempts() {
            clearCallCount++
            storedAttempts.value = emptyList()
        }
    }
}