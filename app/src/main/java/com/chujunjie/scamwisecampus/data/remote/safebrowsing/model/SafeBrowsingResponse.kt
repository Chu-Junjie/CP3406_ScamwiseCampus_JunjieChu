package com.chujunjie.scamwisecampus.data.remote.safebrowsing.model

import kotlinx.serialization.Serializable

@Serializable
data class SafeBrowsingRequest(
    val client: ClientInfo,
    val threatInfo: ThreatInfo
)

@Serializable
data class ClientInfo(
    val clientId: String,
    val clientVersion: String
)

@Serializable
data class ThreatInfo(
    val threatTypes: List<String>,
    val platformTypes: List<String>,
    val threatEntryTypes: List<String>,
    val threatEntries: List<ThreatEntry>
)

@Serializable
data class ThreatEntry(
    val url: String
)

@Serializable
data class SafeBrowsingResponse(
    val matches: List<ThreatMatch> = emptyList()
)

@Serializable
data class ThreatMatch(
    val threatType: String,
    val platformType: String = "",
    val threatEntryType: String = "",
    val threat: ThreatEntry? = null,
    val cacheDuration: String? = null
)