package com.perpetio.well_be.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AvatarModel(var avatarUrl: String?) : Parcelable

