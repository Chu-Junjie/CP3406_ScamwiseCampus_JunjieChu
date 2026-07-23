package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.remote.safebrowsing.SafeBrowsingApi
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import com.chujunjie.scamwisecampus.domain.repository.LinkVerificationRepository

class LinkVerificationRepositoryImpl(
    private val safeBrowsingApi: SafeBrowsingApi,
    private val apiKey: String,
    private val currentTimeMillis: () -> Long = {
        System.currentTimeMillis()
    }
) : LinkVerificationRepository {

    private val memoryCache =
        mutableMapOf<String, CachedResult>()

    override suspend fun checkUrl(
        normalizedUrl: String,
        domain: String
    ): LinkVerificationResult {
        if (apiKey.isBlank()) {
            return LinkVerificationResult.ApiKeyMissing
        }

        val now = currentTimeMillis()

        memoryCache[normalizedUrl]
            ?.takeIf { cachedResult ->
                now < cachedResult.expiresAtEpochMillis
            }
            ?.let { cachedResult ->
                return cachedResult.result
            }

        val response = safeBrowsingApi.searchUrl(
            url = normalizedUrl,
            apiKey = apiKey
        )

        val threatTypes = response.threats
            .flatMap { threat ->
                threat.threatTypes
            }
            .distinct()
            .sorted()

        val result =
            if (threatTypes.isEmpty()) {
                LinkVerificationResult.NoKnownThreat(
                    checkedUrl = normalizedUrl,
                    domain = domain
                )
            } else {
                LinkVerificationResult.PotentialThreat(
                    checkedUrl = normalizedUrl,
                    domain = domain,
                    threatTypes = threatTypes
                )
            }

        val cacheDurationMillis =
            response.cacheDuration
                .toDurationMillis()

        if (cacheDurationMillis > 0) {
            memoryCache[normalizedUrl] =
                CachedResult(
                    result = result,
                    expiresAtEpochMillis =
                        now + cacheDurationMillis
                )
        }

        return result
    }

    private fun String?.toDurationMillis(): Long {
        val seconds = this
            ?.removeSuffix("s")
            ?.toDoubleOrNull()
            ?: return 0

        return (seconds * 1_000)
            .toLong()
            .coerceAtLeast(0)
    }

    private data class CachedResult(
        val result: LinkVerificationResult,
        val expiresAtEpochMillis: Long
    )
}
