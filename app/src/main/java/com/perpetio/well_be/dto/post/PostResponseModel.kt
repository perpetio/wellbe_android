package com.perpetio.well_be.dto.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PostResponseModel(
    val page: Int,
    val pagesNumber: Int,
    val results: List<PostModel>,
    val size: Int
) : Parcelable