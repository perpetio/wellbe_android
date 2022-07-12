package com.perpetio.well_be.dto.post

import android.os.Parcelable
import com.perpetio.well_be.dto.user.PostMemberModel
import com.perpetio.well_be.dto.user.UserPostModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PostModel(
    var id: Long? = null,
    val user: UserPostModel?,
    var title: String,
    val text: String,
    val visible: Boolean,
    val tag: String?,
    val backColor: String,
    var likes: Long,
    var userStatus: PostMemberModel,
    val createdTime: String?,
    val sign: Int,
    val signColor: String?
) : Parcelable
