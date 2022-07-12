package com.perpetio.well_be.repo.user

import com.perpetio.well_be.dto.user.AvatarModel
import com.perpetio.well_be.dto.user.UserUpdatingModel
import com.perpetio.well_be.utils.Result

interface UserRepository {

    suspend fun editUser(userUpdatingModel: UserUpdatingModel): Result<Boolean>

    suspend fun getAvatar(): Result<AvatarModel>

    suspend fun updateAvatar(array: ByteArray): Result<AvatarModel>

    sealed class Endpoints(val url: String) {
        object USER : Endpoints("/user")
        object AVATAR : Endpoints("/avatar")
    }
}