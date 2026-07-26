package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult

@Composable
internal fun LinkVerificationResultContent(
    result: LinkVerificationResult,
    onClearResult: () -> Unit
) {
    when (result) {
        LinkVerificationResult.ApiKeyMissing -> {
            ApiKeyMissingCard()
        }

        is LinkVerificationResult.NoKnownThreat -> {
            NoKnownThreatCard(
                result = result,
                onClearResult = onClearResult
            )
        }

        is LinkVerificationResult.PotentialThreat -> {
            PotentialThreatCard(
                result = result,
                onClearResult = onClearResult
            )
        }
    }
}

@Composable
private fun ApiKeyMissingCard() {
    LinkInformationCard(
        title = stringResource(
            R.string.link_api_key_missing_title
        )
    ) {
        Text(
            text = stringResource(
                R.string.link_api_key_missing_message
            ),
            style =
                MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NoKnownThreatCard(
    result: LinkVerificationResult.NoKnownThreat,
    onClearResult: () -> Unit
) {
    LinkInformationCard(
        title = stringResource(
            R.string.link_no_known_threat_title
        )
    ) {
        Text(
            text = result.domain,
            style =
                MaterialTheme.typography.titleMedium,
            color =
                MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(
                R.string.link_no_known_threat_message
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        Text(
            text = stringResource(
                R.string.link_no_safety_guarantee
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        LinkClearResultButton(
            onClearResult = onClearResult
        )
    }
}

@Composable
private fun PotentialThreatCard(
    result: LinkVerificationResult.PotentialThreat,
    onClearResult: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    LinkInformationCard(
        title = stringResource(
            R.string.link_potential_threat_title
        )
    ) {
        Text(
            text = result.domain,
            style =
                MaterialTheme.typography.titleMedium,
            color =
                MaterialTheme.colorScheme.error
        )

        Text(
            text = stringResource(
                R.string.link_potential_threat_message
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        result.threatTypes.forEach { threatType ->
            Text(
                text = stringResource(
                    R.string.link_threat_bullet_format,
                    threatType.displayName()
                ),
                style =
                    MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 6.dp
                )
            )
        }

        Text(
            text = stringResource(
                R.string.link_threat_safety_advice
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp)
        )

        TextButton(
            onClick = {
                uriHandler.openUri(
                    SAFE_BROWSING_ADVISORY_URL
                )
            },
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.link_google_advisory
                )
            )
        }

        LinkClearResultButton(
            onClearResult = onClearResult
        )
    }
}

private const val SAFE_BROWSING_ADVISORY_URL =
    "https://developers.google.com/safe-browsing/v4/advisory"