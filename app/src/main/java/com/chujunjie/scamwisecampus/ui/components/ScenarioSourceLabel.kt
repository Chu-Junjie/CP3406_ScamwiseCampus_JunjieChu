package com.chujunjie.scamwisecampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

@Composable
fun ScenarioSourceLabel(
    sender: String,
    category: ScamCategory,
    modifier: Modifier = Modifier,
    showFromPrefix: Boolean = false
) {
    val displayedSender =
        if (showFromPrefix) {
            stringResource(
                R.string.practice_sender_format,
                sender
            )
        } else {
            sender
        }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = category.sourceIcon(),
            contentDescription = null,
            tint =
                MaterialTheme.colorScheme
                    .onSurfaceVariant,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
        )

        Text(
            text = displayedSender,
            style = MaterialTheme.typography.bodySmall,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )
    }
}

private fun ScamCategory.sourceIcon(): ImageVector {
    return when (this) {
        ScamCategory.JOB ->
            Icons.Outlined.WorkOutline

        ScamCategory.BANKING ->
            Icons.Outlined.AccountBalance

        ScamCategory.PARCEL ->
            Icons.Outlined.LocalShipping

        ScamCategory.MARKETPLACE ->
            Icons.Outlined.Storefront

        ScamCategory.IMPERSONATION ->
            Icons.Outlined.PersonOutline

        ScamCategory.PHISHING ->
            Icons.Outlined.Email
    }
}