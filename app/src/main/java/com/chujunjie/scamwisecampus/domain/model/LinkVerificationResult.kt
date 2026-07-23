package com.chujunjie.scamwisecampus.domain.model

sealed interface LinkVerificationResult {

    data class NoKnownThreat(
        val checkedUrl: String,
        val domain: String
    ) : LinkVerificationResult

    data class PotentialThreat(
        val checkedUrl: String,
        val domain: String,
        val threatTypes: List<String>
    ) : LinkVerificationResult

    data object ApiKeyMissing : LinkVerificationResult
}
