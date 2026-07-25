package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.annotation.StringRes
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ConfidenceCalibration
import com.chujunjie.scamwisecampus.domain.model.ConfidenceLevel
import com.chujunjie.scamwisecampus.domain.model.RiskLevel

internal fun ScenarioStep.progress(): Float {
    return when (this) {
        ScenarioStep.RISK_ASSESSMENT -> 0.25f
        ScenarioStep.WARNING_SIGNS -> 0.50f
        ScenarioStep.SAFE_ACTION -> 0.75f
        ScenarioStep.CONFIDENCE -> 1.00f
    }
}

@StringRes
internal fun ScenarioStep.displayTitleRes(): Int {
    return when (this) {
        ScenarioStep.RISK_ASSESSMENT ->
            R.string.scenario_step_assess_risk

        ScenarioStep.WARNING_SIGNS ->
            R.string.scenario_step_find_warning_signs

        ScenarioStep.SAFE_ACTION ->
            R.string.scenario_step_choose_action

        ScenarioStep.CONFIDENCE ->
            R.string.scenario_step_rate_confidence
    }
}

@StringRes
internal fun RiskLevel.displayNameRes(): Int {
    return when (this) {
        RiskLevel.HIGH_RISK ->
            R.string.risk_level_high

        RiskLevel.NEEDS_VERIFICATION ->
            R.string.risk_level_needs_verification

        RiskLevel.NO_CLEAR_THREAT ->
            R.string.risk_level_no_clear_threat
    }
}

@StringRes
internal fun ConfidenceLevel.displayNameRes(): Int {
    return when (this) {
        ConfidenceLevel.NOT_CONFIDENT ->
            R.string.confidence_not_confident

        ConfidenceLevel.SOMEWHAT_CONFIDENT ->
            R.string.confidence_somewhat_confident

        ConfidenceLevel.VERY_CONFIDENT ->
            R.string.confidence_very_confident
    }
}

@StringRes
internal fun ConfidenceCalibration.displayNameRes(): Int {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            R.string.calibration_well_calibrated

        ConfidenceCalibration.UNDERCONFIDENT ->
            R.string.calibration_underconfident

        ConfidenceCalibration.OVERCONFIDENT ->
            R.string.calibration_overconfident

        ConfidenceCalibration.NEEDS_REVIEW ->
            R.string.calibration_needs_review

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            R.string.calibration_cautious_but_incorrect
    }
}

@StringRes
internal fun ConfidenceCalibration.descriptionRes(): Int {
    return when (this) {
        ConfidenceCalibration.WELL_CALIBRATED ->
            R.string.calibration_description_well_calibrated

        ConfidenceCalibration.UNDERCONFIDENT ->
            R.string.calibration_description_underconfident

        ConfidenceCalibration.OVERCONFIDENT ->
            R.string.calibration_description_overconfident

        ConfidenceCalibration.NEEDS_REVIEW ->
            R.string.calibration_description_needs_review

        ConfidenceCalibration.CAUTIOUS_BUT_INCORRECT ->
            R.string
                .calibration_description_cautious_but_incorrect
    }
}

@StringRes
internal fun ScenarioValidationError.messageRes(): Int {
    return when (this) {
        ScenarioValidationError.RISK_LEVEL_REQUIRED ->
            R.string.scenario_validation_risk_required

        ScenarioValidationError.WARNING_SIGN_REQUIRED ->
            R.string.scenario_validation_warning_required

        ScenarioValidationError.ACTION_REQUIRED ->
            R.string.scenario_validation_action_required

        ScenarioValidationError.CONFIDENCE_REQUIRED ->
            R.string.scenario_validation_confidence_required
    }
}

@StringRes
internal fun ScenarioSaveStatus.messageRes(): Int {
    return when (this) {
        ScenarioSaveStatus.SAVE_FAILED ->
            R.string.scenario_save_failed_message
    }
}
