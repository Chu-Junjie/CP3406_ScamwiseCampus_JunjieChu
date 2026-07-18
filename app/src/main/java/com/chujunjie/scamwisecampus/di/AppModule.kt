package com.chujunjie.scamwisecampus.di

import com.chujunjie.scamwisecampus.data.repository.ScenarioRepositoryImpl
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<ScenarioRepository> {
        ScenarioRepositoryImpl()
    }

    viewModelOf(::PracticeViewModel)
}