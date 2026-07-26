package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

@Composable
internal fun CategoryFilterRow(
    selectedCategory: ScamCategory?,
    onCategorySelected: (ScamCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = {
                    onCategorySelected(null)
                },
                label = {
                    Text(
                        text = stringResource(
                            R.string.practice_all
                        )
                    )
                }
            )
        }

        items(
            items = ScamCategory.entries,
            key = { category ->
                category.name
            }
        ) { category ->
            FilterChip(
                selected =
                    selectedCategory == category,
                onClick = {
                    onCategorySelected(category)
                },
                label = {
                    Text(
                        text = stringResource(
                            category.displayNameRes()
                        )
                    )
                }
            )
        }
    }
}

@Composable
internal fun DifficultyFilterRow(
    selectedDifficulty: Difficulty?,
    onDifficultySelected: (Difficulty?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected =
                    selectedDifficulty == null,
                onClick = {
                    onDifficultySelected(null)
                },
                label = {
                    Text(
                        text = stringResource(
                            R.string.practice_all
                        )
                    )
                }
            )
        }

        items(
            items = Difficulty.entries,
            key = { difficulty ->
                difficulty.name
            }
        ) { difficulty ->
            FilterChip(
                selected =
                    selectedDifficulty == difficulty,
                onClick = {
                    onDifficultySelected(difficulty)
                },
                label = {
                    Text(
                        text = stringResource(
                            difficulty.displayNameRes()
                        )
                    )
                }
            )
        }
    }
}