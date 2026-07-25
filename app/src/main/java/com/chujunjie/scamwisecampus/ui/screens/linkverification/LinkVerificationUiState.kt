package com.chujunjie.scamwisecampus.ui.screens.linkverification

import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import com.chujunjie.scamwisecampus.domain.model.UrlValidationError

data class LinkVerificationUiState(
    val urlInput: String = "",
    val hasConsent: Boolean = false,
    val isLoading: Boolean = false,
    val result: LinkVerificationResult? = null,
    val validationError: UrlValidationError? = null,
    val statusMessage: LinkVerificationStatusMessage? = null
)

enum class LinkVerificationStatusMessage {
    CONSENT_REQUIRED,
    NETWORK_ERROR
}
