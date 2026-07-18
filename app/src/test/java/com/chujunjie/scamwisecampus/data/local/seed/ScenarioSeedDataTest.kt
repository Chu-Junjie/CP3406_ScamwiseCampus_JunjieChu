package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScenarioSeedDataTest {

    @Test
    fun `catalogue contains eighteen scenarios`() {
        assertEquals(18, ScenarioSeedData.scenarios.size)
    }

    @Test
    fun `scenario identifiers are unique`() {
        val scenarios = ScenarioSeedData.scenarios
        val uniqueIdentifiers = scenarios.map { scenario -> scenario.id }.toSet()

        assertEquals(scenarios.size, uniqueIdentifiers.size)
    }

    @Test
    fun `each category contains three scenarios`() {
        ScamCategory.entries.forEach { category ->
            val categoryCount = ScenarioSeedData.scenarios.count { scenario ->
                scenario.category == category
            }

            assertEquals(
                "Category $category must contain three scenarios.",
                3,
                categoryCount
            )
        }
    }

    @Test
    fun `each category covers every difficulty`() {
        ScamCategory.entries.forEach { category ->
            val difficulties = ScenarioSeedData.scenarios
                .filter { scenario -> scenario.category == category }
                .map { scenario -> scenario.difficulty }
                .toSet()

            assertEquals(
                "Category $category must cover all difficulties.",
                Difficulty.entries.toSet(),
                difficulties
            )
        }
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
    fun `each scenario contains four warning sign options`() {
        ScenarioSeedData.scenarios.forEach { scenario ->
            assertEquals(
                "Scenario ${scenario.id} must contain four warning sign options.",
                4,
                scenario.warningSigns.size
            )
        }
    }

    @Test
    fun `each scenario contains four action options`() {
        ScenarioSeedData.scenarios.forEach { scenario ->
            assertEquals(
                "Scenario ${scenario.id} must contain four action options.",
                4,
                scenario.actionOptions.size
            )
        }
    }

    @Test
    fun `high risk and verification scenarios contain actual warning signs`() {
        ScenarioSeedData.scenarios
            .filter { scenario -> scenario.correctRiskLevel != RiskLevel.NO_CLEAR_THREAT }
            .forEach { scenario ->
                assertTrue(
                    "Scenario ${scenario.id} must contain at least one actual warning sign.",
                    scenario.warningSigns.any { warning -> warning.isActualWarning }
                )
            }
    }

    @Test
    fun `no clear threat scenarios contain no actual warning signs`() {
        ScenarioSeedData.scenarios
            .filter { scenario -> scenario.correctRiskLevel == RiskLevel.NO_CLEAR_THREAT }
            .forEach { scenario ->
                assertTrue(
                    "Scenario ${scenario.id} must not contain an actual warning sign.",
                    scenario.warningSigns.none { warning -> warning.isActualWarning }
                )
            }
    }

    @Test
    fun `catalogue covers all three risk levels`() {
        val riskLevels = ScenarioSeedData.scenarios
            .map { scenario -> scenario.correctRiskLevel }
            .toSet()

        assertEquals(RiskLevel.entries.toSet(), riskLevels)
    }
}
