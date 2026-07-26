package com.chujunjie.scamwisecampus.ui.screens.practice

import androidx.annotation.StringRes
import com.chujunjie.scamwisecampus.R
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.ScamCategory

@StringRes
internal fun ScamCategory.displayNameRes(): Int {
    return when (this) {
        ScamCategory.JOB ->
            R.string.category_job

        ScamCategory.BANKING ->
            R.string.category_banking

        ScamCategory.PARCEL ->
            R.string.category_parcel

        ScamCategory.MARKETPLACE ->
            R.string.category_marketplace

        ScamCategory.IMPERSONATION ->
            R.string.category_impersonation

        ScamCategory.PHISHING ->
            R.string.category_phishing
    }
}

@StringRes
internal fun Difficulty.displayNameRes(): Int {
    return when (this) {
        Difficulty.EASY ->
            R.string.difficulty_easy

        Difficulty.MEDIUM ->
            R.string.difficulty_medium

        Difficulty.HARD ->
            R.string.difficulty_hard
    }
}