package com.chujunjie.scamwisecampus.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
internal fun ClearHistoryDialog(
    isClearing: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isClearing) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = stringResource(
                    R.string
                        .settings_clear_history_dialog_title
                )
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string
                        .settings_clear_history_dialog_message
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isClearing
            ) {
                Text(
                    text = stringResource(
                        R.string
                            .settings_clear_history_confirm
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isClearing
            ) {
                Text(
                    text = stringResource(
                        R.string.common_cancel
                    )
                )
            }
        }
    )
}

@Composable
internal fun LoadingSettingsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.semantics {
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
                R.string.settings_loading
            ),
            modifier =
                Modifier.padding(top = 16.dp)
        )
    }
}