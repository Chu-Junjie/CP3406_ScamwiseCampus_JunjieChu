package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R

@Composable
internal fun LoadingPracticeContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Text(
            text = stringResource(
                R.string.practice_loading_scenarios
            ),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
internal fun EmptyPracticeContent(
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .semantics {
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.practice_no_matching_scenarios
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        TextButton(
            onClick = onClearFilters,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.practice_clear_filters
                )
            )
        }
    }
}