package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.domain.model.UrlValidationError
import com.chujunjie.scamwisecampus.domain.model.UrlValidationResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateUrlUseCaseTest {

    private val validateUrl =
        ValidateUrlUseCase()

    @Test
    fun `blank input returns empty input error`() {
        assertEquals(
            UrlValidationResult.Invalid(
                UrlValidationError.EMPTY_INPUT
            ),
            validateUrl("   ")
        )
    }

    @Test
    fun `missing scheme defaults to https`() {
        assertEquals(
            UrlValidationResult.Valid(
                normalizedUrl =
                    "https://example.com/login",
                domain = "example.com"
            ),
            validateUrl("example.com/login")
        )
    }

    @Test
    fun `fragment is removed before sending`() {
        assertEquals(
            UrlValidationResult.Valid(
                normalizedUrl =
                    "https://example.com/login",
                domain = "example.com"
            ),
            validateUrl(
                "https://example.com/login#private"
            )
        )
    }

    @Test
    fun `unsupported scheme returns typed error`() {
        assertEquals(
            UrlValidationResult.Invalid(
                UrlValidationError.UNSUPPORTED_SCHEME
            ),
            validateUrl("javascript:alert(1)")
        )
    }

    @Test
    fun `url containing spaces returns typed error`() {
        assertEquals(
            UrlValidationResult.Invalid(
                UrlValidationError.CONTAINS_WHITESPACE
            ),
            validateUrl("https://example.com/a b")
        )
    }

    @Test
    fun `host without top level domain is rejected`() {
        assertEquals(
            UrlValidationResult.Invalid(
                UrlValidationError.INVALID_DOMAIN
            ),
            validateUrl("https://localhost/login")
        )
    }
}
