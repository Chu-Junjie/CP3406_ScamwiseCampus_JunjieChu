package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.remote.safebrowsing.SafeBrowsingApi
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.ClientInfo
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingRequest
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.ThreatEntry
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.ThreatInfo
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

        val request = SafeBrowsingRequest(
            client = ClientInfo(
                clientId = "scamwise-campus",
                clientVersion = "1.0.0"
            ),
            threatInfo = ThreatInfo(
                threatTypes = listOf(
                    "MALWARE",
                    "SOCIAL_ENGINEERING",
                    "UNWANTED_SOFTWARE",
                    "POTENTIALLY_HARMFUL_APPLICATION"
                ),
                platformTypes = listOf(
                    "ANY_PLATFORM"
                ),
                threatEntryTypes = listOf(
                    "URL"
                ),
                threatEntries = listOf(
                    ThreatEntry(
                        url = normalizedUrl
                    )
                )
            )
        )

        val response = safeBrowsingApi.searchUrl(
            apiKey = apiKey,
            request = request
        )

        val threatTypes = response.matches
            .map { match ->
                match.threatType
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
            response.matches
                .map { match ->
                    match.cacheDuration
                        .toDurationMillis()
                }
                .filter { duration ->
                    duration > 0
                }
                .minOrNull()
                ?: 0L

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
            ?: return 0L

        return (seconds * 1_000)
            .toLong()
            .coerceAtLeast(0L)
    }

    private data class CachedResult(
        val result: LinkVerificationResult,
        val expiresAtEpochMillis: Long
    )
}