package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chujunjie.scamwisecampus.domain.model.UrlValidationResult
import com.chujunjie.scamwisecampus.domain.repository.LinkVerificationRepository
import com.chujunjie.scamwisecampus.domain.usecase.ValidateUrlUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkVerificationViewModel(
    private val validateUrl: ValidateUrlUseCase,
    private val linkVerificationRepository:
        LinkVerificationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LinkVerificationUiState())

    val uiState: StateFlow<LinkVerificationUiState> =
        _uiState.asStateFlow()

    fun updateUrlInput(urlInput: String) {
        _uiState.update { state ->
            state.copy(
                urlInput = urlInput,
                result = null,
                validationMessage = null,
                networkErrorMessage = null
            )
        }
    }

    fun updateConsent(hasConsent: Boolean) {
        _uiState.update { state ->
            state.copy(
                hasConsent = hasConsent,
                validationMessage = null
            )
        }
    }

    fun checkUrl() {
        val state = _uiState.value

        if (state.isLoading) {
            return
        }

        if (!state.hasConsent) {
            _uiState.update { currentState ->
                currentState.copy(
                    validationMessage =
                        "Confirm that you understand the URL will be sent to Google Safe Browsing."
                )
            }

            return
        }

        when (
            val validationResult =
                validateUrl(state.urlInput)
        ) {
            is UrlValidationResult.Invalid -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        result = null,
                        validationMessage =
                            validationResult.message,
                        networkErrorMessage = null
                    )
                }
            }

            is UrlValidationResult.Valid -> {
                performCheck(validationResult)
            }
        }
    }

    fun clearResult() {
        _uiState.update { state ->
            state.copy(
                result = null,
                validationMessage = null,
                networkErrorMessage = null
            )
        }
    }

    private fun performCheck(
        validationResult: UrlValidationResult.Valid
    ) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    result = null,
                    validationMessage = null,
                    networkErrorMessage = null
                )
            }

            runCatching {
                linkVerificationRepository.checkUrl(
                    normalizedUrl =
                        validationResult.normalizedUrl,
                    domain =
                        validationResult.domain
                )
            }.onSuccess { result ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        result = result
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        result = null,
                        networkErrorMessage =
                            "The URL could not be checked. Confirm your internet connection and try again."
                    )
                }
            }
        }
    }
}
