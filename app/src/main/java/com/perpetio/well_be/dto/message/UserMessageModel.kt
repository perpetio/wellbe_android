package com.perpetio.well_be.dto.message

import android.os.Parcelable
import com.perpetio.well_be.dto.user.AvatarModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserMessageModel(
    val id: Long,
    val username: String,
    var avatarModel: AvatarModel?
) : Parcelable