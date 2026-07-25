package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.domain.model.UrlValidationError
import com.chujunjie.scamwisecampus.domain.model.UrlValidationResult
import java.net.IDN
import java.net.URI

class ValidateUrlUseCase {

    operator fun invoke(
        rawInput: String
    ): UrlValidationResult {
        val trimmedInput = rawInput.trim()

        if (trimmedInput.isBlank()) {
            return invalid(
                UrlValidationError.EMPTY_INPUT
            )
        }

        if (
            trimmedInput.any { character ->
                character.isWhitespace()
            }
        ) {
            return invalid(
                UrlValidationError.CONTAINS_WHITESPACE
            )
        }

        val explicitScheme =
            EXPLICIT_SCHEME_PATTERN
                .find(trimmedInput)
                ?.groupValues
                ?.get(1)
                ?.lowercase()

        if (
            explicitScheme != null &&
            explicitScheme !in SUPPORTED_SCHEMES
        ) {
            return invalid(
                UrlValidationError.UNSUPPORTED_SCHEME
            )
        }

        val candidate =
            if (explicitScheme != null) {
                trimmedInput
            } else {
                "https://$trimmedInput"
            }

        val parsedUri = runCatching {
            URI(candidate)
        }.getOrNull()
            ?: return invalid(
                UrlValidationError.INVALID_WEB_ADDRESS
            )

        val scheme = parsedUri.scheme
            ?.lowercase()
            .orEmpty()

        if (scheme != "http" && scheme != "https") {
            return invalid(
                UrlValidationError.UNSUPPORTED_SCHEME
            )
        }

        val rawHost = parsedUri.host
            ?.lowercase()
            ?.trimEnd('.')
            .orEmpty()

        if (rawHost.isBlank() || !rawHost.contains(".")) {
            return invalid(
                UrlValidationError.INVALID_DOMAIN
            )
        }

        val asciiHost = runCatching {
            IDN.toASCII(rawHost)
        }.getOrNull()
            ?: return invalid(
                UrlValidationError.UNREADABLE_DOMAIN
            )

        val pathValue =
            parsedUri.rawPath
                ?.takeIf { value -> value.isNotBlank() }
                ?: "/"

        val normalizedUri = runCatching {
            URI(
                scheme,
                null,
                asciiHost,
                parsedUri.port,
                pathValue,
                parsedUri.rawQuery,
                null
            )
        }.getOrNull()
            ?: return invalid(
                UrlValidationError.NORMALISATION_FAILED
            )

        return UrlValidationResult.Valid(
            normalizedUrl = normalizedUri.toASCIIString(),
            domain = asciiHost
        )
    }

    private fun invalid(
        error: UrlValidationError
    ): UrlValidationResult.Invalid {
        return UrlValidationResult.Invalid(
            error = error
        )
    }
}

private val SUPPORTED_SCHEMES =
    setOf("http", "https")

private val EXPLICIT_SCHEME_PATTERN =
    Regex("^([A-Za-z][A-Za-z0-9+.-]*):")
