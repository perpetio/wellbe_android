package com.perpetio.well_be.repo.post

import com.perpetio.well_be.dto.post.CreateEditPostModel
import com.perpetio.well_be.dto.post.PostResponseModel
import com.perpetio.well_be.network.FormattedNetworkClientException
import com.perpetio.well_be.network.RestApiBuilder
import com.perpetio.well_be.utils.Result
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PostRepositoryImpl(private val client: RestApiBuilder) : PostRepository {

    override suspend fun createPost(postModel: CreateEditPostModel): Result<CreateEditPostModel> {
        return try {
            Result.Success(client.api.post(PostRepository.Endpoints.POST.url) {
                contentType(ContentType.Application.Json)
                setBody(postModel)
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun editPost(postModel: CreateEditPostModel): Result<Boolean> {
        return try {
            Result.Success(client.api.put(PostRepository.Endpoints.POST.url) {
                contentType(ContentType.Application.Json)
                setBody(postModel)
            }.body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun deletePost(id: Long): Result<Boolean> {
        return try {
            Result.Success(client.api.delete(PostRepository.Endpoints.POST.url + "/$id").body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getMyPosts(): Result<PostResponseModel> {
        return try {
            Result.Success(client.api.get(PostRepository.Endpoints.GET_MY_POSTS.url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getFeedPosts(): Result<PostResponseModel> {
        return try {
            Result.Success(client.api.get(PostRepository.Endpoints.GET_FEED_POSTS.url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getPopularPosts(): Result<PostResponseModel> {
        return try {
            Result.Success(client.api.get(PostRepository.Endpoints.GET_POPULAR_POSTS.url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getFavoritesPosts(): Result<PostResponseModel> {
        return try {
            Result.Success(client.api.get(PostRepository.Endpoints.GET_FAVORITES_POSTS.url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun onLikePost(postId: Long): Result<Boolean> {
        val url = PostRepository.Endpoints.LIKE_DISLIKE.url.replace("{postId}", postId.toString())
        return try {
            Result.Success(client.api.post(url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun onDislikePost(postId: Long): Result<Boolean> {
        val url = PostRepository.Endpoints.LIKE_DISLIKE.url.replace("{postId}", postId.toString())
        return try {
            Result.Success(client.api.delete(url).body())
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }
}