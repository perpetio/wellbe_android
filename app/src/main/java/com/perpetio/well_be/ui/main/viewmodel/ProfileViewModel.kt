package com.perpetio.well_be.ui.main.viewmodel

import androidx.lifecycle.*
import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.dto.user.AvatarModel
import com.perpetio.well_be.dto.user.UserModel
import com.perpetio.well_be.dto.user.UserUpdatingModel
import com.perpetio.well_be.repo.user.UserRepository
import com.perpetio.well_be.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val accManager: AccountModule,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _user: MutableLiveData<UserModel> =
        accManager.getUser().asLiveData() as MutableLiveData<UserModel>
    val user: LiveData<UserModel> get() = _user

    private val _avatar: MutableLiveData<AvatarModel> = MutableLiveData()
    val avatar: LiveData<AvatarModel> get() = _avatar

    private val _editProfileEvent = MutableSharedFlow<Boolean>()
    val editProfileEvent = _editProfileEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            when (val avatarResponse = userRepository.getAvatar()) {
                is Result.Success -> {
                    _avatar.postValue(avatarResponse.data!!)
                }
                is Result.Error -> {
                    _errorEvent.emit(avatarResponse.message.toString())
                }
            }
        }
    }

    fun editUser(username: String?, country: String?, city: String?) {
        val userUpdatingModel = UserUpdatingModel(username, country, city)
        viewModelScope.launch {
            when (val result = userRepository.editUser(userUpdatingModel)) {
                is Result.Success -> {
                    accManager.updateUser(userUpdatingModel)
                    _user.postValue(accManager.getUser().first())
                    result.data?.let { _editProfileEvent.emit(it) }
                }
                is Result.Error -> {
                    _errorEvent.emit(result.message.toString())
                }
            }
        }
    }

    fun updateAvatar(array: ByteArray) {
        viewModelScope.launch {
            when (val avatarResponse = userRepository.updateAvatar(array)) {
                is Result.Success -> {
                    _avatar.postValue(avatarResponse.data!!)
                }
                is Result.Error -> {
                    _errorEvent.emit(avatarResponse.message.toString())
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            accManager.clearData()
            _logoutEvent.emit(Unit)
        }
    }

}