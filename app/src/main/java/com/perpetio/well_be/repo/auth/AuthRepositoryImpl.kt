package com.perpetio.well_be.repo.auth

import com.perpetio.well_be.dto.user.UserCredModel
import com.perpetio.well_be.dto.user.UserLoginModel
import com.perpetio.well_be.dto.user.UserRegistrationModel
import com.perpetio.well_be.network.FormattedNetworkClientException
import com.perpetio.well_be.network.RestApiBuilder
import com.perpetio.well_be.utils.Result
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRepositoryImpl(private val client: RestApiBuilder) : AuthRepository {

    override suspend fun login(loginModel: UserLoginModel): Result<UserCredModel?> {
        return try {
            Result.Success(client.api.post(AuthRepository.Endpoints.LOGIN.url) {
                contentType(ContentType.Application.Json)
                setBody(loginModel)
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun registration(registrationModel: UserRegistrationModel): Result<UserCredModel?> {
        return try {
            Result.Success(client.api.post(AuthRepository.Endpoints.REGISTRATION.url) {
                contentType(ContentType.Application.Json)
                setBody(registrationModel)
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }
}