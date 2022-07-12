package com.perpetio.well_be.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.dto.user.UserLoginModel
import com.perpetio.well_be.dto.user.UserRegistrationModel
import com.perpetio.well_be.repo.auth.AuthRepository
import com.perpetio.well_be.ui.auth.AuthEventType
import com.perpetio.well_be.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val accManager: AccountModule,
    private val authRepository: AuthRepository
) : ViewModel() {

    val authEvent = MutableLiveData<AuthEventType>()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun login(loginModel: UserLoginModel) {
        viewModelScope.launch {
            when (val result = authRepository.login(loginModel)) {
                is Result.Success -> {
                    result.data?.let { accManager.saveUser(it) }
                    authEvent.postValue(AuthEventType.LOGGED_IN)
                }
                is Result.Error -> {
                    _errorEvent.emit(result.message.toString())
                }
            }
        }
    }

    fun registration(registrationModel: UserRegistrationModel) {
        viewModelScope.launch {
            when (val registration = authRepository.registration(registrationModel)) {
                is Result.Success -> {
                    registration.data?.let { accManager.saveUser(it) }
                    authEvent.postValue(AuthEventType.SIGNED_UP)
                }
                is Result.Error -> {
                    _errorEvent.emit(registration.message.toString())
                }
            }
        }
    }

}