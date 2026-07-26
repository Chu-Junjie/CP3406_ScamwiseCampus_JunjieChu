package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.UrlValidationError

@Composable
internal fun LinkVerificationBackButton(
    onNavigateBack: () -> Unit
) {
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

@Composable
internal fun LinkVerificationHeader() {
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

@Composable
internal fun BeforeYouCheckCard() {
    LinkInformationCard(
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

@Composable
internal fun LinkUrlInput(
    urlInput: String,
    isLoading: Boolean,
    onUrlInputChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = urlInput,
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
                    R.string.link_url_supporting_text
                )
            )
        },
        singleLine = true,
        enabled = !isLoading,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
internal fun LinkConsentCard(
    hasConsent: Boolean,
    isLoading: Boolean,
    onConsentChanged: (Boolean) -> Unit
) {
    ElevatedCard(
        onClick = {
            onConsentChanged(!hasConsent)
        },
        enabled = !isLoading,
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
                checked = hasConsent,
                onCheckedChange = null
            )

            Text(
                text = stringResource(
                    R.string.link_consent_text
                ),
                style =
                    MaterialTheme.typography.bodyMedium,
                modifier =
                    Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
internal fun LinkValidationErrorText(
    error: UrlValidationError
) {
    Text(
        text = stringResource(
            error.messageRes()
        ),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.semantics {
            liveRegion = LiveRegionMode.Assertive
        }
    )
}

@Composable
internal fun LinkStatusMessageCard(
    statusMessage: LinkVerificationStatusMessage
) {
    LinkInformationCard(
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
                liveRegion = LiveRegionMode.Assertive
            }
        )
    }
}

@Composable
internal fun LinkCheckButton(
    isLoading: Boolean,
    onCheckUrl: () -> Unit
) {
    Button(
        onClick = onCheckUrl,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )

            Text(
                text = stringResource(
                    R.string.link_checking
                ),
                modifier =
                    Modifier.padding(start = 8.dp)
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

@Composable
internal fun LinkLearningReminderCard() {
    LinkInformationCard(
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