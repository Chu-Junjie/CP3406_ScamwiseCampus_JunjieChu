package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.Scenario

object ScenarioSeedData {

    val scenarios: List<Scenario> =
        JobScenarioSeed.scenarios +
                BankingScenarioSeed.scenarios +
                ParcelScenarioSeed.scenarios +
                MarketplaceScenarioSeed.scenarios +
                ImpersonationScenarioSeed.scenarios +
                PhishingScenarioSeed.scenarios
}