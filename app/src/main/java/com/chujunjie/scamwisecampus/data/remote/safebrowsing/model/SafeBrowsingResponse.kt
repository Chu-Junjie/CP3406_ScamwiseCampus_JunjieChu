package com.chujunjie.scamwisecampus.data.remote.safebrowsing.model

import kotlinx.serialization.Serializable

@Serializable
data class SafeBrowsingResponse(
    val threats: List<ThreatUrlDto> = emptyList(),
    val cacheDuration: String? = null
)

@Serializable
data class ThreatUrlDto(
    val url: String = "",
    val threatTypes: List<String> = emptyList()
)
