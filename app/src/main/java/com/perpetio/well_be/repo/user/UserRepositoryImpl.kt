package com.perpetio.well_be.repo.user

import com.perpetio.well_be.dto.user.AvatarModel
import com.perpetio.well_be.dto.user.UserUpdatingModel
import com.perpetio.well_be.network.FormattedNetworkClientException
import com.perpetio.well_be.network.RestApiBuilder
import com.perpetio.well_be.utils.Result
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class UserRepositoryImpl(private val client: RestApiBuilder) : UserRepository {

    override suspend fun editUser(userUpdatingModel: UserUpdatingModel): Result<Boolean> {
        return try {
            Result.Success(client.api.put(UserRepository.Endpoints.USER.url) {
                contentType(ContentType.Application.Json)
                setBody(userUpdatingModel)
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getAvatar(): Result<AvatarModel> {
        return try {
            Result.Success(client.api.get(UserRepository.Endpoints.AVATAR.url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun updateAvatar(array: ByteArray): Result<AvatarModel> {
        return try {
            Result.Success(client.api.post(UserRepository.Endpoints.AVATAR.url) {
                setBody(MultiPartFormDataContent(formData {
                    append("document", array, Headers.build {
                        append(HttpHeaders.ContentType, "images/*") // Mime type required
                        append(HttpHeaders.ContentDisposition, "filename=avatar")
                    })
                }))
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            println(exception)
            Result.Error("Server or network error")
        }
    }

}