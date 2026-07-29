package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ScenarioRepositoryImpl : ScenarioRepository {

    override fun observeScenarios(): Flow<List<Scenario>> {
        return flowOf(ScenarioSeedData.scenarios)
    }

    override fun getScenarioById(scenarioId: String): Scenario? {
        return ScenarioSeedData.scenarios.firstOrNull { scenario ->
            scenario.id == scenarioId
        }
    }
}