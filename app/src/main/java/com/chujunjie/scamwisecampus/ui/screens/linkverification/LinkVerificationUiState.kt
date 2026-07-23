package com.chujunjie.scamwisecampus.ui.screens.linkverification

import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult

data class LinkVerificationUiState(
    val urlInput: String = "",
    val hasConsent: Boolean = false,
    val isLoading: Boolean = false,
    val result: LinkVerificationResult? = null,
    val validationMessage: String? = null,
    val networkErrorMessage: String? = null
)
