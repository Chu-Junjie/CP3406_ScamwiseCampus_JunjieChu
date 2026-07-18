package com.chujunjie.scamwisecampus.domain.model

data class ActionOption(
    val id: String,
    val description: String,
    val isSafeAction: Boolean,
    val feedback: String
)