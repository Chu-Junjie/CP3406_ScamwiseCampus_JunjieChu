package com.chujunjie.scamwisecampus.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AttemptWithSelections(
    @Embedded
    val attempt: AttemptEntity,
    @Relation(
        parentColumn = "attempt_id",
        entityColumn = "attempt_id"
    )
    val warningSelections: List<AttemptWarningSelectionEntity>
)
