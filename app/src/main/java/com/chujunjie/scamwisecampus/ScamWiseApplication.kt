package com.chujunjie.scamwisecampus

import android.app.Application
import com.chujunjie.scamwisecampus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScamWiseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ScamWiseApplication)
            modules(appModule)
        }
    }
}