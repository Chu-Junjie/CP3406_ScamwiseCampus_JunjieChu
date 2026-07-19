package com.chujunjie.scamwisecampus.domain.repository

import com.chujunjie.scamwisecampus.domain.model.AttemptRecord
import kotlinx.coroutines.flow.Flow

interface AttemptRepository {

    fun observeAttempts(): Flow<List<AttemptRecord>>

    suspend fun saveAttempt(attemptRecord: AttemptRecord): Long

    suspend fun clearAttempts()
}
