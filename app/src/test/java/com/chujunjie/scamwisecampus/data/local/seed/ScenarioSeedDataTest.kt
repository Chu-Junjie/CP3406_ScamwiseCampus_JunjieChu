package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScenarioSeedDataTest {

    @Test
    fun `scenario identifiers are unique`() {
        val scenarios = ScenarioSeedData.scenarios
        val uniqueIdentifiers = scenarios.map { scenario -> scenario.id }.toSet()

        assertEquals(scenarios.size, uniqueIdentifiers.size)
    }

    @Test
    fun `each scenario contains exactly one safe action`() {
        ScenarioSeedData.scenarios.forEach { scenario ->
            val safeActionCount = scenario.actionOptions.count { action ->
                action.isSafeAction
            }

            assertEquals(
                "Scenario ${scenario.id} must contain exactly one safe action.",
                1,
                safeActionCount
            )
        }
    }

    @Test
    fun `sample data covers all three risk levels`() {
        val riskLevels = ScenarioSeedData.scenarios
            .map { scenario -> scenario.correctRiskLevel }
            .toSet()

        assertEquals(
            setOf(
                RiskLevel.HIGH_RISK,
                RiskLevel.NEEDS_VERIFICATION,
                RiskLevel.NO_CLEAR_THREAT
            ),
            riskLevels
        )
    }

    @Test
    fun `each scenario contains warning sign options`() {
        ScenarioSeedData.scenarios.forEach { scenario ->
            assertTrue(
                "Scenario ${scenario.id} must contain warning sign options.",
                scenario.warningSigns.isNotEmpty()
            )
        }
    }

    @Test
    fun `each scenario contains action options`() {
        ScenarioSeedData.scenarios.forEach { scenario ->
            assertTrue(
                "Scenario ${scenario.id} must contain action options.",
                scenario.actionOptions.isNotEmpty()
            )
        }
    }
}