package com.chujunjie.scamwisecampus.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.chujunjie.scamwisecampus.data.local.database.ScamWiseDatabase
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AttemptRepositoryRoomTest {

    private lateinit var database: ScamWiseDatabase
    private lateinit var repository: AttemptRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        database = Room.inMemoryDatabaseBuilder(
            context,
            ScamWiseDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repository = AttemptRepositoryImpl(
            attemptDao = database.attemptDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveAttempt_readsSameAttemptAndWarningSelections() =
        runBlocking {
            val originalAttempt = sampleAttempt()

            val generatedId = repository.saveAttempt(
                originalAttempt
            )

            val storedAttempt = repository
                .observeAttempts()
                .first()
                .single()

            assertTrue(generatedId > 0)
            assertEquals(
                originalAttempt.copy(
                    attemptId = generatedId
                ),
                storedAttempt
            )
        }

    @Test
    fun clearAttempts_removesStoredHistory() = runBlocking {
        repository.saveAttempt(sampleAttempt())

        assertEquals(
            1,
            repository.observeAttempts().first().size
        )

        repository.clearAttempts()

        assertTrue(
            repository.observeAttempts().first().isEmpty()
        )
    }

    private fun sampleAttempt(): AttemptRecord {
        return AttemptRecord(
            scenarioId = "job_easy_1",
            category = ScamCategory.JOB,
            difficulty = Difficulty.EASY,
            selectedRiskLevel = RiskLevel.HIGH_RISK,
            correctRiskLevel = RiskLevel.HIGH_RISK,
            selectedWarningSignIds = setOf(
                "urgent-payment",
                "unverified-sender"
            ),
            selectedNoWarningSigns = false,
            selectedActionId = "verify-independently",
            confidenceLevel = ConfidenceLevel.VERY_CONFIDENT,
            riskScore = 40,
            warningSignScore = 30,
            safeActionScore = 30,
            totalScore = 100,
            isRiskCorrect = true,
            isSafeActionCorrect = true,
            confidenceCalibration =
                ConfidenceCalibration.WELL_CALIBRATED,
            completedAtEpochMillis = 1_725_000_000_000
        )
    }
}
