package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.UrlValidationError

@Composable
internal fun String.displayName(): String {
    val knownNameRes = knownThreatNameRes()

    return if (knownNameRes != null) {
        stringResource(knownNameRes)
    } else {
        lowercase()
            .replace('_', ' ')
            .replaceFirstChar { character ->
                character.uppercase()
            }
    }
}

@StringRes
private fun String.knownThreatNameRes(): Int? {
    return when (this) {
        "SOCIAL_ENGINEERING" ->
            R.string.link_threat_social_engineering

        "MALWARE" ->
            R.string.link_threat_malware

        "UNWANTED_SOFTWARE" ->
            R.string.link_threat_unwanted_software

        "POTENTIALLY_HARMFUL_APPLICATION" ->
            R.string.link_threat_harmful_application

        else -> null
    }
}

@StringRes
internal fun UrlValidationError.messageRes(): Int {
    return when (this) {
        UrlValidationError.EMPTY_INPUT ->
            R.string.link_validation_empty_input

        UrlValidationError.CONTAINS_WHITESPACE ->
            R.string.link_validation_contains_spaces

        UrlValidationError.INVALID_WEB_ADDRESS ->
            R.string.link_validation_invalid_address

        UrlValidationError.UNSUPPORTED_SCHEME ->
            R.string.link_validation_unsupported_scheme

        UrlValidationError.INVALID_DOMAIN ->
            R.string.link_validation_invalid_domain

        UrlValidationError.UNREADABLE_DOMAIN ->
            R.string.link_validation_unreadable_domain

        UrlValidationError.NORMALISATION_FAILED ->
            R.string.link_validation_normalisation_failed
    }
}

@StringRes
internal fun LinkVerificationStatusMessage.messageRes(): Int {
    return when (this) {
        LinkVerificationStatusMessage.CONSENT_REQUIRED ->
            R.string.link_validation_consent_required

        LinkVerificationStatusMessage.NETWORK_ERROR ->
            R.string.link_network_error
    }
}