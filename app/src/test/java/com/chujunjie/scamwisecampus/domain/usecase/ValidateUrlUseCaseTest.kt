package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.domain.model.UrlValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateUrlUseCaseTest {

    private val validateUrl =
        ValidateUrlUseCase()

    @Test
    fun `blank input is rejected`() {
        val result = validateUrl("   ")

        assertTrue(
            result is UrlValidationResult.Invalid
        )
    }

    @Test
    fun `missing scheme defaults to https`() {
        val result =
            validateUrl("example.com/login")

        assertEquals(
            UrlValidationResult.Valid(
                normalizedUrl =
                    "https://example.com/login",
                domain = "example.com"
            ),
            result
        )
    }

    @Test
    fun `fragment is removed before sending`() {
        val result = validateUrl(
            "https://example.com/login#private"
        )

        assertEquals(
            UrlValidationResult.Valid(
                normalizedUrl =
                    "https://example.com/login",
                domain = "example.com"
            ),
            result
        )
    }

    @Test
    fun `unsupported scheme is rejected`() {
        val result =
            validateUrl("javascript:alert(1)")

        assertTrue(
            result is UrlValidationResult.Invalid
        )
    }

    @Test
    fun `url containing spaces is rejected`() {
        val result =
            validateUrl("https://example.com/a b")

        assertTrue(
            result is UrlValidationResult.Invalid
        )
    }
}
