package com.chujunjie.scamwisecampus.ui.screens.linkverification

import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import com.chujunjie.scamwisecampus.domain.model.UrlValidationError
import com.chujunjie.scamwisecampus.domain.repository.LinkVerificationRepository
import com.chujunjie.scamwisecampus.domain.usecase.ValidateUrlUseCase
import com.chujunjie.scamwisecampus.testutil.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LinkVerificationViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()

    @Test
    fun `check requires user consent`() {
        val repository =
            FakeLinkVerificationRepository()

        val viewModel =
            createViewModel(repository)

        viewModel.updateUrlInput("example.com")
        viewModel.checkUrl()

        assertEquals(
            LinkVerificationStatusMessage.CONSENT_REQUIRED,
            viewModel.uiState.value.statusMessage
        )

        assertEquals(0, repository.checkCount)
    }

    @Test
    fun `valid input is normalised before repository check`() {
        val repository =
            FakeLinkVerificationRepository()

        val viewModel =
            createViewModel(repository)

        viewModel.updateUrlInput(
            "example.com/login#section"
        )
        viewModel.updateConsent(true)
        viewModel.checkUrl()

        assertEquals(
            "https://example.com/login",
            repository.lastCheckedUrl
        )

        assertEquals(
            "example.com",
            repository.lastCheckedDomain
        )
    }

    @Test
    fun `no known threat result is exposed`() {
        val expectedResult =
            LinkVerificationResult.NoKnownThreat(
                checkedUrl =
                    "https://example.com/",
                domain = "example.com"
            )

        val repository =
            FakeLinkVerificationRepository(
                result = expectedResult
            )

        val viewModel =
            createViewModel(repository)

        viewModel.updateUrlInput("example.com")
        viewModel.updateConsent(true)
        viewModel.checkUrl()

        assertEquals(
            expectedResult,
            viewModel.uiState.value.result
        )

        assertFalse(
            viewModel.uiState.value.isLoading
        )
    }

    @Test
    fun `invalid input exposes typed validation error`() {
        val repository =
            FakeLinkVerificationRepository()

        val viewModel =
            createViewModel(repository)

        viewModel.updateUrlInput("not a url")
        viewModel.updateConsent(true)
        viewModel.checkUrl()

        assertEquals(0, repository.checkCount)
        assertNull(viewModel.uiState.value.result)

        assertEquals(
            UrlValidationError.CONTAINS_WHITESPACE,
            viewModel.uiState.value.validationError
        )
    }

    @Test
    fun `network failure exposes recoverable status`() {
        val repository =
            FakeLinkVerificationRepository(
                shouldFail = true
            )

        val viewModel =
            createViewModel(repository)

        viewModel.updateUrlInput("example.com")
        viewModel.updateConsent(true)
        viewModel.checkUrl()

        assertEquals(
            LinkVerificationStatusMessage.NETWORK_ERROR,
            viewModel.uiState.value.statusMessage
        )

        assertFalse(
            viewModel.uiState.value.isLoading
        )
    }

    private fun createViewModel(
        repository: LinkVerificationRepository
    ): LinkVerificationViewModel {
        return LinkVerificationViewModel(
            validateUrl = ValidateUrlUseCase(),
            linkVerificationRepository = repository
        )
    }

    private class FakeLinkVerificationRepository(
        private val result:
        LinkVerificationResult =
            LinkVerificationResult.NoKnownThreat(
                checkedUrl =
                    "https://example.com/",
                domain = "example.com"
            ),
        private val shouldFail:
        Boolean = false
    ) : LinkVerificationRepository {

        var checkCount: Int = 0
            private set

        var lastCheckedUrl: String? = null
            private set

        var lastCheckedDomain: String? = null
            private set

        override suspend fun checkUrl(
            normalizedUrl: String,
            domain: String
        ): LinkVerificationResult {
            checkCount += 1
            lastCheckedUrl = normalizedUrl
            lastCheckedDomain = domain

            if (shouldFail) {
                error("Simulated network failure.")
            }

            return result
        }
    }
}
