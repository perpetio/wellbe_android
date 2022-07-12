package com.perpetio.well_be

import android.app.Application
import com.perpetio.well_be.koin.accountModule
import com.perpetio.well_be.koin.networkModule
import com.perpetio.well_be.koin.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WellBeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WellBeApplication)
            modules(accountModule, viewModelsModule, networkModule)
        }
    }
}