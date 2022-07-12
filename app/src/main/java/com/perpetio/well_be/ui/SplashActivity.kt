package com.perpetio.well_be.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.ui.auth.AuthActivity
import com.perpetio.well_be.ui.main.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val accManager: AccountModule by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (accManager.getToken().isNotEmpty()) {
            MainActivity.startWithSingleTop(this)
        } else AuthActivity.start(this)
    }

}