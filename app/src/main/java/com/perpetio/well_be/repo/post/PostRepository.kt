package com.perpetio.well_be.repo.post

import com.perpetio.well_be.dto.post.CreateEditPostModel
import com.perpetio.well_be.dto.post.PostResponseModel
import com.perpetio.well_be.utils.Result

interface PostRepository {

    suspend fun createPost(postModel: CreateEditPostModel): Result<CreateEditPostModel>

    suspend fun editPost(postModel: CreateEditPostModel): Result<Boolean>

    suspend fun deletePost(id: Long): Result<Boolean>

    suspend fun getMyPosts(): Result<PostResponseModel>

    suspend fun getFeedPosts(): Result<PostResponseModel>

    suspend fun getPopularPosts(): Result<PostResponseModel>

    suspend fun getFavoritesPosts(): Result<PostResponseModel>

    suspend fun onLikePost(postId: Long): Result<Boolean>

    suspend fun onDislikePost(postId: Long): Result<Boolean>

    sealed class Endpoints(val url: String) {
        object POST : Endpoints("/post")
        object GET_MY_POSTS : Endpoints("/my_posts")
        object GET_FEED_POSTS : Endpoints("/feed_posts")
        object GET_POPULAR_POSTS : Endpoints("/popular_posts")
        object GET_FAVORITES_POSTS : Endpoints("/my_favourite_posts")
        object LIKE_DISLIKE : Endpoints("post/{postId}/like")
    }

}