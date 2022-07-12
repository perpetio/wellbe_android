package com.perpetio.well_be.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserModel(
    val id: Long,
    val username: String,
    val mail: String?,
    val country: String?,
    val city: String?
) : Parcelable