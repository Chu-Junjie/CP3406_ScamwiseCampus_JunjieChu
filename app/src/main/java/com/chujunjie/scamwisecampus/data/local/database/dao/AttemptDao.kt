package com.chujunjie.scamwisecampus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWarningSelectionEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWithSelections
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AttemptDao {

    @Insert
    abstract suspend fun insertAttemptEntity(
        attemptEntity: AttemptEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertWarningSelectionEntities(
        warningSelections: List<AttemptWarningSelectionEntity>
    )

    @Transaction
    open suspend fun insertAttemptWithSelections(
        attemptEntity: AttemptEntity,
        selectedWarningSignIds: Set<String>
    ): Long {
        val attemptId = insertAttemptEntity(attemptEntity)

        val warningSelections = selectedWarningSignIds.map { warningSignId ->
            AttemptWarningSelectionEntity(
                attemptId = attemptId,
                warningSignId = warningSignId
            )
        }

        if (warningSelections.isNotEmpty()) {
            insertWarningSelectionEntities(warningSelections)
        }

        return attemptId
    }

    @Transaction
    @Query(
        "SELECT * FROM attempts " +
            "ORDER BY completed_at_epoch_millis DESC"
    )
    abstract fun observeAttemptsWithSelections():
        Flow<List<AttemptWithSelections>>

    @Query("DELETE FROM attempts")
    abstract suspend fun clearAttempts()
}
