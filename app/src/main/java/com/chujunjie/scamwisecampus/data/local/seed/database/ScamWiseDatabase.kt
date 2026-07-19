package com.chujunjie.scamwisecampus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chujunjie.scamwisecampus.data.local.database.dao.AttemptDao
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptEntity
import com.chujunjie.scamwisecampus.data.local.database.entity.AttemptWarningSelectionEntity

@Database(
    entities = [
        AttemptEntity::class,
        AttemptWarningSelectionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ScamWiseDatabase : RoomDatabase() {

    abstract fun attemptDao(): AttemptDao

    companion object {
        const val DATABASE_NAME = "scamwise_database"
    }
}
