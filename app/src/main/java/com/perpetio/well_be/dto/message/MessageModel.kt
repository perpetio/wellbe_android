package com.perpetio.well_be.dto.message

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class MessageModel(
    val id: Long? = null,
    val userId: Long,
    val roomId: Long,
    val text: String,
    val createdTime: String,
    var user: UserMessageModel
) : Parcelable
