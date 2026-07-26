package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.remote.safebrowsing.SafeBrowsingApi
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingRequest
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.SafeBrowsingResponse
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.model.ThreatMatch
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import java.util.ArrayDeque
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LinkVerificationRepositoryImplTest {

    @Test
    fun `blank api key skips network and returns missing key`() = runTest {
        val api = FakeSafeBrowsingApi()
        val repository = LinkVerificationRepositoryImpl(
            safeBrowsingApi = api,
            apiKey = "   "
        )

        val result = repository.checkUrl(
            normalizedUrl = "https://example.com/",
            domain = "example.com"
        )

        assertEquals(
            LinkVerificationResult.ApiKeyMissing,
            result
        )
        assertEquals(0, api.callCount)
    }

    @Test
    fun `empty matches returns no known threat and sends expected request`() =
        runTest {
            val api = FakeSafeBrowsingApi(
                SafeBrowsingResponse()
            )
            val repository = LinkVerificationRepositoryImpl(
                safeBrowsingApi = api,
                apiKey = "test-key"
            )

            val result = repository.checkUrl(
                normalizedUrl = "https://example.com/login",
                domain = "example.com"
            )

            assertEquals(
                LinkVerificationResult.NoKnownThreat(
                    checkedUrl = "https://example.com/login",
                    domain = "example.com"
                ),
                result
            )
            assertEquals(1, api.callCount)
            assertEquals("test-key", api.lastApiKey)

            val request = api.lastRequest
            assertNotNull(request)
            assertEquals(
                "scamwise-campus",
                request?.client?.clientId
            )
            assertEquals(
                listOf("ANY_PLATFORM"),
                request?.threatInfo?.platformTypes
            )
            assertEquals(
                "https://example.com/login",
                request?.threatInfo
                    ?.threatEntries
                    ?.single()
                    ?.url
            )
        }

    @Test
    fun `threat types are distinct and sorted`() = runTest {
        val api = FakeSafeBrowsingApi(
            SafeBrowsingResponse(
                matches = listOf(
                    threat("SOCIAL_ENGINEERING"),
                    threat("MALWARE"),
                    threat("SOCIAL_ENGINEERING")
                )
            )
        )
        val repository = LinkVerificationRepositoryImpl(
            safeBrowsingApi = api,
            apiKey = "test-key"
        )

        val result = repository.checkUrl(
            normalizedUrl = "https://danger.example/",
            domain = "danger.example"
        )

        assertTrue(
            result is LinkVerificationResult.PotentialThreat
        )
        assertEquals(
            listOf(
                "MALWARE",
                "SOCIAL_ENGINEERING"
            ),
            (result as LinkVerificationResult.PotentialThreat)
                .threatTypes
        )
    }

    @Test
    fun `cached result avoids repeat request before expiry`() = runTest {
        var currentTime = 1_000L
        val response = SafeBrowsingResponse(
            matches = listOf(
                threat(
                    type = "MALWARE",
                    cacheDuration = "10s"
                )
            )
        )
        val api = FakeSafeBrowsingApi(response)
        val repository = LinkVerificationRepositoryImpl(
            safeBrowsingApi = api,
            apiKey = "test-key",
            currentTimeMillis = { currentTime }
        )

        val firstResult = repository.checkUrl(
            normalizedUrl = "https://cached.example/",
            domain = "cached.example"
        )

        currentTime = 10_999L

        val secondResult = repository.checkUrl(
            normalizedUrl = "https://cached.example/",
            domain = "cached.example"
        )

        assertEquals(firstResult, secondResult)
        assertEquals(1, api.callCount)
    }

    @Test
    fun `shortest positive cache duration controls expiry`() = runTest {
        var currentTime = 0L
        val api = FakeSafeBrowsingApi(
            SafeBrowsingResponse(
                matches = listOf(
                    threat(
                        type = "MALWARE",
                        cacheDuration = "10s"
                    ),
                    threat(
                        type = "SOCIAL_ENGINEERING",
                        cacheDuration = "2s"
                    )
                )
            ),
            SafeBrowsingResponse()
        )
        val repository = LinkVerificationRepositoryImpl(
            safeBrowsingApi = api,
            apiKey = "test-key",
            currentTimeMillis = { currentTime }
        )

        repository.checkUrl(
            normalizedUrl = "https://expiry.example/",
            domain = "expiry.example"
        )

        currentTime = 1_999L
        repository.checkUrl(
            normalizedUrl = "https://expiry.example/",
            domain = "expiry.example"
        )

        assertEquals(1, api.callCount)

        currentTime = 2_000L
        val resultAfterExpiry = repository.checkUrl(
            normalizedUrl = "https://expiry.example/",
            domain = "expiry.example"
        )

        assertEquals(2, api.callCount)
        assertEquals(
            LinkVerificationResult.NoKnownThreat(
                checkedUrl = "https://expiry.example/",
                domain = "expiry.example"
            ),
            resultAfterExpiry
        )
    }

    @Test
    fun `non-positive cache duration is not cached`() = runTest {
        val api = FakeSafeBrowsingApi(
            SafeBrowsingResponse(
                matches = listOf(
                    threat(
                        type = "MALWARE",
                        cacheDuration = "0s"
                    )
                )
            ),
            SafeBrowsingResponse()
        )
        val repository = LinkVerificationRepositoryImpl(
            safeBrowsingApi = api,
            apiKey = "test-key"
        )

        repository.checkUrl(
            normalizedUrl = "https://uncached.example/",
            domain = "uncached.example"
        )

        val secondResult = repository.checkUrl(
            normalizedUrl = "https://uncached.example/",
            domain = "uncached.example"
        )

        assertEquals(2, api.callCount)
        assertEquals(
            LinkVerificationResult.NoKnownThreat(
                checkedUrl = "https://uncached.example/",
                domain = "uncached.example"
            ),
            secondResult
        )
    }

    private fun threat(
        type: String,
        cacheDuration: String? = null
    ): ThreatMatch {
        return ThreatMatch(
            threatType = type,
            cacheDuration = cacheDuration
        )
    }

    private class FakeSafeBrowsingApi(
        vararg responses: SafeBrowsingResponse
    ) : SafeBrowsingApi {

        private val queuedResponses =
            ArrayDeque(responses.toList())

        var callCount: Int = 0
            private set

        var lastApiKey: String? = null
            private set

        var lastRequest: SafeBrowsingRequest? = null
            private set

        override suspend fun searchUrl(
            apiKey: String,
            request: SafeBrowsingRequest
        ): SafeBrowsingResponse {
            callCount += 1
            lastApiKey = apiKey
            lastRequest = request

            return if (queuedResponses.isEmpty()) {
                SafeBrowsingResponse()
            } else {
                queuedResponses.removeFirst()
            }
        }
    }
}