package com.perpetio.well_be.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserCredModel(
    val id: Long,
    val username: String,
    val mail: String? = null,
    val country: String? = null,
    val city: String? = null,
    val token: String? = null,
    val avatarModel: AvatarModel
) : Parcelable