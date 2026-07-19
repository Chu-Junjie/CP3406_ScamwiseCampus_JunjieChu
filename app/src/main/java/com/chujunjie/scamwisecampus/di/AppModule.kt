package com.chujunjie.scamwisecampus.di

import androidx.room.Room
import com.chujunjie.scamwisecampus.data.local.database.ScamWiseDatabase
import com.chujunjie.scamwisecampus.data.repository.AttemptRepositoryImpl
import com.chujunjie.scamwisecampus.data.repository.ScenarioRepositoryImpl
import com.chujunjie.scamwisecampus.domain.repository.AttemptRepository
import com.chujunjie.scamwisecampus.domain.repository.ScenarioRepository
import com.chujunjie.scamwisecampus.domain.usecase.EvaluateScenarioAttemptUseCase
import com.chujunjie.scamwisecampus.ui.screens.practice.PracticeViewModel
import com.chujunjie.scamwisecampus.ui.screens.scenario.ScenarioActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.chujunjie.scamwisecampus.ui.screens.statistics.StatisticsViewModel

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            ScamWiseDatabase::class.java,
            ScamWiseDatabase.DATABASE_NAME
        ).build()
    }

    single {
        get<ScamWiseDatabase>().attemptDao()
    }

    single<AttemptRepository> {
        AttemptRepositoryImpl(
            attemptDao = get()
        )
    }

    single<ScenarioRepository> {
        ScenarioRepositoryImpl()
    }

    factory {
        EvaluateScenarioAttemptUseCase()
    }

    viewModelOf(::PracticeViewModel)

    viewModelOf(::ScenarioActivityViewModel)

    viewModelOf(::StatisticsViewModel)
}
