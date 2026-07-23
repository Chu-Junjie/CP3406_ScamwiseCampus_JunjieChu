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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.chujunjie.scamwisecampus.data.preferences.settingsDataStore
import com.chujunjie.scamwisecampus.data.repository.SettingsRepositoryImpl
import com.chujunjie.scamwisecampus.domain.repository.SettingsRepository
import com.chujunjie.scamwisecampus.ui.screens.settings.SettingsViewModel
import com.chujunjie.scamwisecampus.ui.theme.AppThemeViewModel
import com.chujunjie.scamwisecampus.ui.screens.home.HomeViewModel
import com.chujunjie.scamwisecampus.BuildConfig
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.AndroidAppHeadersInterceptor
import com.chujunjie.scamwisecampus.data.remote.safebrowsing.SafeBrowsingApi
import com.chujunjie.scamwisecampus.data.repository.LinkVerificationRepositoryImpl
import com.chujunjie.scamwisecampus.domain.repository.LinkVerificationRepository
import com.chujunjie.scamwisecampus.domain.usecase.ValidateUrlUseCase
import com.chujunjie.scamwisecampus.ui.screens.linkverification.LinkVerificationViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

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

    single<DataStore<Preferences>> {
        androidContext().settingsDataStore
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            dataStore = get()
        )
    }

    factory {
        EvaluateScenarioAttemptUseCase()
    }

    single {
        AndroidAppHeadersInterceptor(
            context = androidContext()
        )
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(
                get<AndroidAppHeadersInterceptor>()
            )
            .build()
    }

    single {
        val json = Json {
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl(
                "https://safebrowsing.googleapis.com/"
            )
            .client(get())
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }

    single {
        get<Retrofit>().create(
            SafeBrowsingApi::class.java
        )
    }

    single<LinkVerificationRepository> {
        LinkVerificationRepositoryImpl(
            safeBrowsingApi = get(),
            apiKey =
                BuildConfig.SAFE_BROWSING_API_KEY
        )
    }

    factory {
        ValidateUrlUseCase()
    }

    viewModelOf(::LinkVerificationViewModel)

    viewModelOf(::HomeViewModel)

    viewModelOf(::PracticeViewModel)

    viewModelOf(::ScenarioActivityViewModel)

    viewModelOf(::StatisticsViewModel)

    viewModelOf(::SettingsViewModel)

    viewModelOf(::AppThemeViewModel)
}
