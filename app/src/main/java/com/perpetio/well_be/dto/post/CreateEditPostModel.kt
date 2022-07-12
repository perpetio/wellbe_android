package com.perpetio.well_be.dto.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CreateEditPostModel(
    var id: Long? = null,
    val title: String,
    val text: String,
    val visible: Boolean,
    val tag: String?,
    val backColor: String,
    val likes: Long,
    val sign: Int?,
    val signColor: String?
) : Parcelable