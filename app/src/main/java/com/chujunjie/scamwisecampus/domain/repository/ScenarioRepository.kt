package com.chujunjie.scamwisecampus.domain.repository

import com.chujunjie.scamwisecampus.domain.model.Scenario
import kotlinx.coroutines.flow.Flow

interface ScenarioRepository {

    fun observeScenarios(): Flow<List<Scenario>>

    fun getScenarioById(scenarioId: String): Scenario?
}