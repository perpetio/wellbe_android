package com.perpetio.well_be.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.ActivityAuthBinding
import com.perpetio.well_be.ui.auth.viewmodel.AuthViewModel
import com.perpetio.well_be.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val authViewModel by viewModel<AuthViewModel>()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }

        fun launchLogout(context: Context) =
            Intent(context, AuthActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                .let(context::startActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController(R.id.nav_host_fragment_activity_auth)
        lifecycleScope.launchWhenStarted {
            authViewModel.errorEvent.collect {
                Toast.makeText(this@AuthActivity, it, Toast.LENGTH_LONG).show()
            }
        }
        authViewModel.authEvent.observe(this) {
            if (it == AuthEventType.LOGGED_IN) {
                Toast.makeText(this, getString(R.string.log_in_successfully), Toast.LENGTH_LONG).show()
            } else Toast.makeText(this, getString(R.string.sign_up_successfully), Toast.LENGTH_LONG).show()
            MainActivity.startWithSingleTop(this)
        }
    }
}