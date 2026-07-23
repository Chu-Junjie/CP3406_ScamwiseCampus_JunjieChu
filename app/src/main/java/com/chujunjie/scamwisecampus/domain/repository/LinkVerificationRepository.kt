package com.chujunjie.scamwisecampus.domain.repository

import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult

interface LinkVerificationRepository {

    suspend fun checkUrl(
        normalizedUrl: String,
        domain: String
    ): LinkVerificationResult
}
