package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.local.database.dao.AttemptDao
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWithSelections
import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttemptRepositoryImpl(
    private val attemptDao: AttemptDao
) : AttemptRepository {

    override fun observeAttempts(): Flow<List<AttemptRecord>> {
        return attemptDao.observeAttemptsWithSelections()
            .map { storedAttempts ->
                storedAttempts.map { storedAttempt ->
                    storedAttempt.toDomain()
                }
            }
    }

    override suspend fun saveAttempt(
        attemptRecord: AttemptRecord
    ): Long {
        return attemptDao.insertAttemptWithSelections(
            attemptEntity = attemptRecord.toEntity(),
            selectedWarningSignIds =
                attemptRecord.selectedWarningSignIds
        )
    }

    override suspend fun clearAttempts() {
        attemptDao.clearAttempts()
    }
}

private fun AttemptRecord.toEntity(): AttemptEntity {
    return AttemptEntity(
        attemptId = attemptId,
        scenarioId = scenarioId,
        category = category.name,
        difficulty = difficulty.name,
        selectedRiskLevel = selectedRiskLevel.name,
        correctRiskLevel = correctRiskLevel.name,
        selectedNoWarningSigns = selectedNoWarningSigns,
        selectedActionId = selectedActionId,
        confidenceLevel = confidenceLevel.name,
        riskScore = riskScore,
        warningSignScore = warningSignScore,
        safeActionScore = safeActionScore,
        totalScore = totalScore,
        isRiskCorrect = isRiskCorrect,
        isSafeActionCorrect = isSafeActionCorrect,
        confidenceCalibration = confidenceCalibration.name,
        completedAtEpochMillis = completedAtEpochMillis
    )
}

private fun AttemptWithSelections.toDomain(): AttemptRecord {
    return AttemptRecord(
        attemptId = attempt.attemptId,
        scenarioId = attempt.scenarioId,
        category = ScamCategory.valueOf(attempt.category),
        difficulty = Difficulty.valueOf(attempt.difficulty),
        selectedRiskLevel =
            RiskLevel.valueOf(attempt.selectedRiskLevel),
        correctRiskLevel =
            RiskLevel.valueOf(attempt.correctRiskLevel),
        selectedWarningSignIds = warningSelections
            .map { selection -> selection.warningSignId }
            .toSet(),
        selectedNoWarningSigns =
            attempt.selectedNoWarningSigns,
        selectedActionId = attempt.selectedActionId,
        confidenceLevel =
            ConfidenceLevel.valueOf(attempt.confidenceLevel),
        riskScore = attempt.riskScore,
        warningSignScore = attempt.warningSignScore,
        safeActionScore = attempt.safeActionScore,
        totalScore = attempt.totalScore,
        isRiskCorrect = attempt.isRiskCorrect,
        isSafeActionCorrect = attempt.isSafeActionCorrect,
        confidenceCalibration = ConfidenceCalibration.valueOf(
            attempt.confidenceCalibration
        ),
        completedAtEpochMillis =
            attempt.completedAtEpochMillis
    )
}
