package com.chujunjie.scamwisecampus.di

import com.chujunjie.scamwisecampus.data.repository.ScenarioRepositoryImpl
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeViewModel
import com.chujunjie.scamwisecampus.ui.screens.scenario.ScenarioActivityViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<ScenarioRepository> {
        ScenarioRepositoryImpl()
    }

    factory {
        EvaluateScenarioAttemptUseCase()
    }

    viewModelOf(::PracticeViewModel)

    viewModelOf(::ScenarioActivityViewModel)
}