package com.chujunjie.scamwisecampus.ui.screens.scenario

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.WarningSign

@Composable
internal fun WarningSignFeedback(
    actualWarningSigns: List<WarningSign>,
    correctlySelectedWarningSigns: List<WarningSign>,
    incorrectlySelectedWarningSigns: List<WarningSign>,
    missedWarningSigns: List<WarningSign>,
    selectedNoWarningSigns: Boolean
) {
    when {
        actualWarningSigns.isEmpty() &&
            selectedNoWarningSigns -> {
            FeedbackLabel(isCorrect = true)

            Text(
                text = stringResource(
                    R.string.result_warning_none_correct
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        actualWarningSigns.isEmpty() -> {
            FeedbackLabel(isCorrect = false)

            Text(
                text = stringResource(
                    R.string.result_warning_none
                ),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 12.dp)
            )

            FeedbackList(
                heading = stringResource(
                    R.string
                        .result_indicators_incorrectly_selected
                ),
                warningSigns =
                    incorrectlySelectedWarningSigns
            )
        }

        else -> {
            if (
                correctlySelectedWarningSigns.isNotEmpty()
            ) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_correctly_identified
                    ),
                    warningSigns =
                        correctlySelectedWarningSigns
                )
            }

            if (missedWarningSigns.isNotEmpty()) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_missed_warning_signs
                    ),
                    warningSigns = missedWarningSigns
                )
            }

            if (
                incorrectlySelectedWarningSigns.isNotEmpty()
            ) {
                FeedbackList(
                    heading = stringResource(
                        R.string.result_incorrectly_selected
                    ),
                    warningSigns =
                        incorrectlySelectedWarningSigns
                )
            }

            if (
                correctlySelectedWarningSigns.size ==
                actualWarningSigns.size &&
                incorrectlySelectedWarningSigns.isEmpty()
            ) {
                Text(
                    text = stringResource(
                        R.string
                            .result_all_warning_signs_correct
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FeedbackList(
    heading: String,
    warningSigns: List<WarningSign>
) {
    if (warningSigns.isEmpty()) {
        return
    }

    Text(
        text = heading,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(top = 12.dp)
    )

    warningSigns.forEach { warningSign ->
        Text(
            text = stringResource(
                R.string.result_bullet_format,
                warningSign.description
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 6.dp
            )
        )
    }
}
