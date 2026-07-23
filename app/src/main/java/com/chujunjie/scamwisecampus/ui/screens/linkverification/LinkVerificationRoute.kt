package com.chujunjie.scamwisecampus.ui.screens.linkverification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun LinkVerificationRoute(
    onNavigateBack: () -> Unit,
    viewModel: LinkVerificationViewModel =
        koinViewModel()
) {
    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LinkVerificationScreen(
        uiState = uiState,
        onUrlInputChanged =
            viewModel::updateUrlInput,
        onConsentChanged =
            viewModel::updateConsent,
        onCheckUrl =
            viewModel::checkUrl,
        onClearResult =
            viewModel::clearResult,
        onNavigateBack =
            onNavigateBack
    )
}
