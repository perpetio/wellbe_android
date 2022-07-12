package com.perpetio.well_be.dto.message

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MessageResponseModel(
    val page: Int,
    val pagesNumber: Int,
    val results: List<MessageModel>,
    val size: Int
) : Parcelable