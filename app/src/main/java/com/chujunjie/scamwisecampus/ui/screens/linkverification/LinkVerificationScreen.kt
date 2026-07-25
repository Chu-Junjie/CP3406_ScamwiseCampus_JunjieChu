package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult
import com.chujunjie.scamwisecampus.domain.model.UrlValidationError
import com.chujunjie.scamwisecampus.ui.components.ResponsiveContent

@Composable
fun LinkVerificationScreen(
    uiState: LinkVerificationUiState,
    onUrlInputChanged: (String) -> Unit,
    onConsentChanged: (Boolean) -> Unit,
    onCheckUrl: () -> Unit,
    onClearResult: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveContent(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 12.dp,
                end = 16.dp,
                bottom = 32.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            item {
                TextButton(
                    onClick = onNavigateBack
                ) {
                    Text(
                        text = stringResource(
                            R.string.common_back
                        )
                    )
                }
            }

            item {
                Text(
                    text = stringResource(
                        R.string.link_verification_title
                    ),
                    style =
                        MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                Text(
                    text = stringResource(
                        R.string.link_verification_description
                    ),
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            item {
                InformationCard(
                    title = stringResource(
                        R.string.link_before_check_title
                    )
                ) {
                    Text(
                        text = stringResource(
                            R.string.link_full_url_notice
                        ),
                        style =
                            MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = stringResource(
                            R.string
                                .link_remove_personal_information
                        ),
                        style =
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.link_limitations_notice
                        ),
                        style =
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.urlInput,
                    onValueChange = onUrlInputChanged,
                    label = {
                        Text(
                            text = stringResource(
                                R.string.link_url_label
                            )
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.link_url_placeholder
                            )
                        )
                    },
                    supportingText = {
                        Text(
                            text = stringResource(
                                R.string
                                    .link_url_supporting_text
                            )
                        )
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                ElevatedCard(
                    onClick = {
                        onConsentChanged(
                            !uiState.hasConsent
                        )
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(
                            mergeDescendants = true
                        ) {
                            // Merge checkbox state and consent text.
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uiState.hasConsent,
                            onCheckedChange = null
                        )

                        Text(
                            text = stringResource(
                                R.string.link_consent_text
                            ),
                            style =
                                MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    }
                }
            }

            uiState.validationError?.let { error ->
                item {
                    Text(
                        text = stringResource(
                            error.messageRes()
                        ),
                        color =
                            MaterialTheme.colorScheme.error,
                        style =
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.semantics {
                            liveRegion =
                                LiveRegionMode.Assertive
                        }
                    )
                }
            }

            uiState.statusMessage?.let { statusMessage ->
                item {
                    InformationCard(
                        title = stringResource(
                            R.string.link_check_unavailable
                        )
                    ) {
                        Text(
                            text = stringResource(
                                statusMessage.messageRes()
                            ),
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.semantics {
                                liveRegion =
                                    LiveRegionMode.Assertive
                            }
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = onCheckUrl,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )

                        Text(
                            text = stringResource(
                                R.string.link_checking
                            ),
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(
                                R.string.link_check_url
                            )
                        )
                    }
                }
            }

            uiState.result?.let { result ->
                item {
                    Box(
                        modifier = Modifier.semantics {
                            liveRegion =
                                LiveRegionMode.Polite
                        }
                    ) {
                        ResultContent(
                            result = result,
                            onClearResult = onClearResult
                        )
                    }
                }
            }

            item {
                InformationCard(
                    title = stringResource(
                        R.string.link_learning_reminder_title
                    )
                ) {
                    Text(
                        text = stringResource(
                            R.string.link_learning_reminder
                        ),
                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultContent(
    result: LinkVerificationResult,
    onClearResult: () -> Unit
) {
    when (result) {
        LinkVerificationResult.ApiKeyMissing -> {
            InformationCard(
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

        is LinkVerificationResult.NoKnownThreat -> {
            InformationCard(
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

                ClearResultButton(
                    onClearResult = onClearResult
                )
            }
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
private fun PotentialThreatCard(
    result: LinkVerificationResult.PotentialThreat,
    onClearResult: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    InformationCard(
        title = stringResource(
            R.string.link_potential_threat_title
        )
    ) {
        Text(
            text = result.domain,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            text = stringResource(
                R.string.link_potential_threat_message
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        result.threatTypes.forEach { threatType ->
            Text(
                text = stringResource(
                    R.string.link_threat_bullet_format,
                    threatType.displayName()
                ),
                style = MaterialTheme.typography.bodyMedium,
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
            style = MaterialTheme.typography.bodyMedium,
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

        ClearResultButton(
            onClearResult = onClearResult
        )
    }
}

@Composable
private fun ClearResultButton(
    onClearResult: () -> Unit
) {
    OutlinedButton(
        onClick = onClearResult,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(
            text = stringResource(
                R.string.link_check_another_url
            )
        )
    }
}

@Composable
private fun InformationCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}

@Composable
private fun String.displayName(): String {
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
private fun UrlValidationError.messageRes(): Int {
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
private fun LinkVerificationStatusMessage.messageRes(): Int {
    return when (this) {
        LinkVerificationStatusMessage.CONSENT_REQUIRED ->
            R.string.link_validation_consent_required

        LinkVerificationStatusMessage.NETWORK_ERROR ->
            R.string.link_network_error
    }
}

private const val SAFE_BROWSING_ADVISORY_URL =
    "https://developers.google.com/safe-browsing/v4/advisory"
