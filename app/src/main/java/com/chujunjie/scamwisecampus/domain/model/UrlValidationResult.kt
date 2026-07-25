package com.chujunjie.scamwisecampus.domain.model

sealed interface UrlValidationResult {

    data class Valid(
        val normalizedUrl: String,
        val domain: String
    ) : UrlValidationResult

    data class Invalid(
        val error: UrlValidationError
    ) : UrlValidationResult
}

enum class UrlValidationError {
    EMPTY_INPUT,
    CONTAINS_WHITESPACE,
    INVALID_WEB_ADDRESS,
    UNSUPPORTED_SCHEME,
    INVALID_DOMAIN,
    UNREADABLE_DOMAIN,
    NORMALISATION_FAILED
}
