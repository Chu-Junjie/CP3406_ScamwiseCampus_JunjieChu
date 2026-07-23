package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.domain.model.LinkVerificationResult

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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(
                onClick = onNavigateBack
            ) {
                Text(text = "Back")
            }
        }

        item {
            Text(
                text = "Link Verification Lab",
                style =
                    MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Check whether a web address matches a known Google Safe Browsing threat.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        item {
            InformationCard(
                title = "Before You Check"
            ) {
                Text(
                    text = "The full URL, including its path and query, is sent to Google Safe Browsing only after you press Check URL.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Remove personal tokens or identifying information from the URL when possible.",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "Safe Browsing may miss risky sites or occasionally flag a legitimate site. A result is guidance, not a guarantee.",
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
                    Text(text = "URL")
                },
                placeholder = {
                    Text(
                        text = "https://example.com/path"
                    )
                },
                supportingText = {
                    Text(
                        text = "HTTP and HTTPS web addresses only."
                    )
                },
                singleLine = true,
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType =
                        KeyboardType.Uri,
                    imeAction =
                        ImeAction.Done
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
                modifier = Modifier.fillMaxWidth()
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
                        text = "I understand that this URL will be sent to Google Safe Browsing for this check.",
                        style =
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            start = 8.dp
                        )
                    )
                }
            }
        }

        uiState.validationMessage?.let { message ->
            item {
                Text(
                    text = message,
                    color =
                        MaterialTheme.colorScheme.error,
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }

        uiState.networkErrorMessage?.let { message ->
            item {
                InformationCard(
                    title = "Check Unavailable"
                ) {
                    Text(
                        text = message,
                        style =
                            MaterialTheme.typography.bodyMedium
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
                    CircularProgressIndicator()
                } else {
                    Text(text = "Check URL")
                }
            }
        }

        uiState.result?.let { result ->
            item {
                ResultContent(
                    result = result,
                    onClearResult = onClearResult
                )
            }
        }

        item {
            InformationCard(
                title = "Learning Reminder"
            ) {
                Text(
                    text = "A link check is only one signal. Also verify the sender, domain spelling, request, urgency, and safest independent action.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
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
                title = "API Key Not Configured"
            ) {
                Text(
                    text = "Add the Safe Browsing API key to local.properties and rebuild the app.",
                    style =
                        MaterialTheme.typography.bodyMedium
                )
            }
        }

        is LinkVerificationResult.NoKnownThreat -> {
            InformationCard(
                title = "No Known Threat Match Found"
            ) {
                Text(
                    text = result.domain,
                    style =
                        MaterialTheme.typography.titleMedium,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Google Safe Browsing did not return a known threat match for this URL.",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "This does not prove that the page is safe. Continue to verify the sender and purpose independently.",
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
        title = "Potential Threat Match Found"
    ) {
        Text(
            text = result.domain,
            style =
                MaterialTheme.typography.titleMedium,
            color =
                MaterialTheme.colorScheme.error
        )

        Text(
            text = "This URL may be associated with one or more known threat categories.",
            style =
                MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        result.threatTypes.forEach { threatType ->
            Text(
                text = "• ${threatType.displayName()}",
                style =
                    MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 6.dp
                )
            )
        }

        Text(
            text = "Do not sign in, download files, or submit payment details. Verify the service through an official app or manually entered address.",
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
                text = "Advisory provided by Google"
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
        Text(text = "Check Another URL")
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
                style =
                    MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.padding(top = 12.dp),
                content = content
            )
        }
    }
}

private fun String.displayName(): String {
    return when (this) {
        "SOCIAL_ENGINEERING" ->
            "Social engineering or phishing"

        "MALWARE" ->
            "Malware"

        "UNWANTED_SOFTWARE" ->
            "Unwanted software"

        "POTENTIALLY_HARMFUL_APPLICATION" ->
            "Potentially harmful application"

        else ->
            lowercase()
                .replace('_', ' ')
                .replaceFirstChar { character ->
                    character.uppercase()
                }
    }
}

private const val SAFE_BROWSING_ADVISORY_URL =
    "https://developers.google.com/safe-browsing/v4/advisory"
