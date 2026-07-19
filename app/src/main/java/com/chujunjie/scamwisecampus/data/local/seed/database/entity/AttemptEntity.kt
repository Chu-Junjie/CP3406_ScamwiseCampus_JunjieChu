package com.chujunjie.scamwisecampus.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attempts",
    indices = [
        Index(value = ["scenario_id"]),
        Index(value = ["completed_at_epoch_millis"])
    ]
)
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "attempt_id")
    val attemptId: Long = 0,
    @ColumnInfo(name = "scenario_id")
    val scenarioId: String,
    val category: String,
    val difficulty: String,
    @ColumnInfo(name = "selected_risk_level")
    val selectedRiskLevel: String,
    @ColumnInfo(name = "correct_risk_level")
    val correctRiskLevel: String,
    @ColumnInfo(name = "selected_no_warning_signs")
    val selectedNoWarningSigns: Boolean,
    @ColumnInfo(name = "selected_action_id")
    val selectedActionId: String,
    @ColumnInfo(name = "confidence_level")
    val confidenceLevel: String,
    @ColumnInfo(name = "risk_score")
    val riskScore: Int,
    @ColumnInfo(name = "warning_sign_score")
    val warningSignScore: Int,
    @ColumnInfo(name = "safe_action_score")
    val safeActionScore: Int,
    @ColumnInfo(name = "total_score")
    val totalScore: Int,
    @ColumnInfo(name = "is_risk_correct")
    val isRiskCorrect: Boolean,
    @ColumnInfo(name = "is_safe_action_correct")
    val isSafeActionCorrect: Boolean,
    @ColumnInfo(name = "confidence_calibration")
    val confidenceCalibration: String,
    @ColumnInfo(name = "completed_at_epoch_millis")
    val completedAtEpochMillis: Long
)
