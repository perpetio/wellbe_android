package com.perpetio.well_be.repo.auth

import com.perpetio.well_be.dto.user.UserCredModel
import com.perpetio.well_be.dto.user.UserLoginModel
import com.perpetio.well_be.dto.user.UserRegistrationModel
import com.perpetio.well_be.utils.Result

interface AuthRepository {

    suspend fun login(loginModel: UserLoginModel): Result<UserCredModel?>

    suspend fun registration(registrationModel: UserRegistrationModel): Result<UserCredModel?>

    sealed class Endpoints(val url: String) {
        object LOGIN : Endpoints("/login")
        object REGISTRATION : Endpoints("/register")
    }
}