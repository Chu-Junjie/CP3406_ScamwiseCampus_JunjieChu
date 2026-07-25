package com.chujunjie.scamwisecampus.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R

@Composable
internal fun TwoColumnRow(
    itemCount: Int,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        content()

        if (itemCount == 1) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
internal fun SectionHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.semantics {
            heading()
        }
    )
}

@Composable
internal fun StatisticsCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
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

@Composable
internal fun SummaryValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Read the metric label and value together.
            },
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
internal fun ProgressMetric(
    label: String,
    percent: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(
                mergeDescendants = true
            ) {
                // Read the metric label and percentage together.
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style =
                    MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(
                    R.string.percentage_format,
                    percent
                ),
                style =
                    MaterialTheme.typography.labelLarge
            )
        }

        LinearProgressIndicator(
            progress = {
                percent.coerceIn(
                    minimumValue = 0,
                    maximumValue = 100
                ) / 100f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clearAndSetSemantics {
                    // The visible percentage describes progress.
                }
        )
    }
}

@Composable
internal fun CalibrationRow(
    label: String,
    count: Int
) {
    SummaryValueRow(
        label = label,
        value = count.toString(),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}