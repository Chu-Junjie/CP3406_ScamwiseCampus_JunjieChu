package com.chujunjie.scamwisecampus.domain.model

data class Scenario(
    val id: String,
    val title: String,
    val category: ScamCategory,
    val difficulty: Difficulty,
    val messageType: MessageType,
    val sender: String,
    val subject: String? = null,
    val messageBody: String,
    val correctRiskLevel: RiskLevel,
    val warningSigns: List<WarningSign>,
    val actionOptions: List<ActionOption>,
    val explanation: String,
    val verificationAdvice: String
)