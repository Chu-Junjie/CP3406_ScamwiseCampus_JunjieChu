package com.chujunjie.scamwisecampus.domain.model

sealed interface UrlValidationResult {

    data class Valid(
        val normalizedUrl: String,
        val domain: String
    ) : UrlValidationResult

    data class Invalid(
        val message: String
    ) : UrlValidationResult
}
