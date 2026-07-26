package com.chujunjie.scamwisecampus.data.repository

import com.chujunjie.scamwisecampus.data.local.seed.ScenarioSeedData
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class ScenarioRepositoryImplTest {

    private val repository =
        ScenarioRepositoryImpl()

    @Test
    fun `observe scenarios emits the complete seed catalogue`() =
        runTest {
            val emissions =
                repository.observeScenarios().toList()

            assertEquals(1, emissions.size)
            assertEquals(
                ScenarioSeedData.scenarios,
                emissions.single()
            )
        }

    @Test
    fun `observe scenarios preserves catalogue order`() =
        runTest {
            val observedIds =
                repository.observeScenarios()
                    .toList()
                    .single()
                    .map { scenario ->
                        scenario.id
                    }

            val expectedIds =
                ScenarioSeedData.scenarios.map {
                        scenario ->
                    scenario.id
                }

            assertEquals(
                expectedIds,
                observedIds
            )
        }

    @Test
    fun `get scenario by id returns the matching seed instance`() {
        val expectedScenario =
            ScenarioSeedData.scenarios.first()

        val result =
            repository.getScenarioById(
                expectedScenario.id
            )

        assertSame(
            expectedScenario,
            result
        )
    }

    @Test
    fun `get scenario by id returns null for an unknown id`() {
        val result =
            repository.getScenarioById(
                "scenario_that_does_not_exist"
            )

        assertNull(result)
    }

    @Test
    fun `scenario id lookup remains exact and case sensitive`() {
        val existingId =
            ScenarioSeedData.scenarios.first().id

        val result =
            repository.getScenarioById(
                existingId.uppercase()
            )

        if (existingId != existingId.uppercase()) {
            assertNull(result)
        } else {
            assertSame(
                ScenarioSeedData.scenarios.first(),
                result
            )
        }
    }

    @Test
    fun `empty scenario id returns null`() {
        assertNull(
            repository.getScenarioById("")
        )
    }
}