package com.chujunjie.scamwisecampus.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "attempt_warning_selections",
    primaryKeys = [
        "attempt_id",
        "warning_sign_id"
    ],
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["attempt_id"],
            childColumns = ["attempt_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["attempt_id"])
    ]
)
data class AttemptWarningSelectionEntity(
    @ColumnInfo(name = "attempt_id")
    val attemptId: Long,
    @ColumnInfo(name = "warning_sign_id")
    val warningSignId: String
)
