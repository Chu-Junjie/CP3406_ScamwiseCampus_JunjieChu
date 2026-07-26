package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
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
                LinkVerificationBackButton(
                    onNavigateBack = onNavigateBack
                )
            }

            item {
                LinkVerificationHeader()
            }

            item {
                BeforeYouCheckCard()
            }

            item {
                LinkUrlInput(
                    urlInput = uiState.urlInput,
                    isLoading = uiState.isLoading,
                    onUrlInputChanged =
                        onUrlInputChanged
                )
            }

            item {
                LinkConsentCard(
                    hasConsent = uiState.hasConsent,
                    isLoading = uiState.isLoading,
                    onConsentChanged =
                        onConsentChanged
                )
            }

            uiState.validationError?.let { error ->
                item {
                    LinkValidationErrorText(
                        error = error
                    )
                }
            }

            uiState.statusMessage?.let {
                    statusMessage ->
                item {
                    LinkStatusMessageCard(
                        statusMessage =
                            statusMessage
                    )
                }
            }

            item {
                LinkCheckButton(
                    isLoading = uiState.isLoading,
                    onCheckUrl = onCheckUrl
                )
            }

            uiState.result?.let { result ->
                item {
                    Box(
                        modifier = Modifier.semantics {
                            liveRegion =
                                LiveRegionMode.Polite
                        }
                    ) {
                        LinkVerificationResultContent(
                            result = result,
                            onClearResult =
                                onClearResult
                        )
                    }
                }
            }

            item {
                LinkLearningReminderCard()
            }
        }
    }
}