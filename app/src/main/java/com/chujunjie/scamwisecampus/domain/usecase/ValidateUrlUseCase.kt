package com.chujunjie.scamwisecampus.domain.usecase

import com.chujunjie.scamwisecampus.domain.model.UrlValidationResult
import java.net.IDN
import java.net.URI

class ValidateUrlUseCase {

    operator fun invoke(
        rawInput: String
    ): UrlValidationResult {
        val trimmedInput = rawInput.trim()

        if (trimmedInput.isBlank()) {
            return UrlValidationResult.Invalid(
                message = "Enter a URL to check."
            )
        }

        if (trimmedInput.any { character ->
                character.isWhitespace()
            }
        ) {
            return UrlValidationResult.Invalid(
                message = "The URL must not contain spaces."
            )
        }

        val candidate =
            if (trimmedInput.contains("://")) {
                trimmedInput
            } else {
                "https://$trimmedInput"
            }

        val parsedUri = runCatching {
            URI(candidate)
        }.getOrNull()
            ?: return UrlValidationResult.Invalid(
                message = "Enter a valid web address."
            )

        val scheme = parsedUri.scheme
            ?.lowercase()
            .orEmpty()

        if (scheme != "http" && scheme != "https") {
            return UrlValidationResult.Invalid(
                message = "Only HTTP and HTTPS URLs can be checked."
            )
        }

        val rawHost = parsedUri.host
            ?.lowercase()
            ?.trimEnd('.')
            .orEmpty()

        if (rawHost.isBlank() || !rawHost.contains(".")) {
            return UrlValidationResult.Invalid(
                message = "Enter a URL with a valid domain."
            )
        }

        val asciiHost = runCatching {
            IDN.toASCII(rawHost)
        }.getOrNull()
            ?: return UrlValidationResult.Invalid(
                message = "The domain could not be read."
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
            ?: return UrlValidationResult.Invalid(
                message = "The URL could not be normalised."
            )

        return UrlValidationResult.Valid(
            normalizedUrl = normalizedUri.toASCIIString(),
            domain = asciiHost
        )
    }
}
