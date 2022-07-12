package com.perpetio.well_be.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.dto.post.CreateEditPostModel
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.dto.user.PostMemberModel
import com.perpetio.well_be.repo.post.PostRepository
import com.perpetio.well_be.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val postRepository: PostRepository,
    private val accManager: AccountModule
) : ViewModel() {

    private val _feed_post: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    val feedPosts: LiveData<MutableList<PostModel>> get() = _feed_post

    private val _my_post: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    val myPosts: LiveData<MutableList<PostModel>> get() = _my_post

    private val _popular_post: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    val popularPosts: LiveData<MutableList<PostModel>> get() = _popular_post

    private val _favorites_post: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    val favoritesPosts: LiveData<MutableList<PostModel>> get() = _favorites_post

    private val _postActionEvent = MutableSharedFlow<Unit>()
    val postActionEvent = _postActionEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    init {
        loadFeedPosts()
    }

    val userId: Long get() = accManager.getUserId()

    fun loadFeedPosts() {
        viewModelScope.launch {
            when (val feedPosts = postRepository.getFeedPosts()) {
                is Result.Success -> {
                    _feed_post.postValue(feedPosts.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(feedPosts.message.toString())
                }
            }
        }
    }

    fun loadPopularPosts() {
        viewModelScope.launch {
            when (val popularPosts = postRepository.getPopularPosts()) {
                is Result.Success -> {
                    _popular_post.postValue(popularPosts.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(popularPosts.message.toString())
                }
            }
        }
    }

    fun loadFavoritesPosts() {
        viewModelScope.launch {
            when (val popularPosts = postRepository.getFavoritesPosts()) {
                is Result.Success -> {
                    _favorites_post.postValue(popularPosts.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(popularPosts.message.toString())
                }
            }
        }
    }

    fun createPost(postModel: CreateEditPostModel) {
        viewModelScope.launch {
            when (val createPost = postRepository.createPost(postModel)) {
                is Result.Success -> {
                    _postActionEvent.emit(Unit)
                }
                is Result.Error -> {
                    _errorEvent.emit(createPost.message.toString())
                }
            }
        }
    }

    fun editPost(postModel: CreateEditPostModel) {
        viewModelScope.launch {
            when (val editPost = postRepository.editPost(postModel)) {
                is Result.Success -> {
                    _postActionEvent.emit(Unit)
                }
                is Result.Error -> {
                    _errorEvent.emit(editPost.message.toString())
                }
            }
        }
    }

    fun loadMyPosts() {
        viewModelScope.launch {
            when (val myPosts = postRepository.getMyPosts()) {
                is Result.Success -> {
                    _my_post.postValue(myPosts.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(myPosts.message.toString())
                }
            }
        }
    }

    fun onLikePost(postModel: PostModel) {
        viewModelScope.launch {
            when (val onLikedPost = postRepository.onLikePost(postModel.id!!)) {
                is Result.Success -> {
                    updateLikeState(postModel)
                }
                is Result.Error -> {
                    _errorEvent.emit(onLikedPost.message.toString())
                }
            }
        }
    }

    fun onDislikePost(postModel: PostModel) {
        viewModelScope.launch {
            when (val onDislikePost = postRepository.onDislikePost(postModel.id!!)) {
                is Result.Success -> {
                    updateLikeState(postModel)
                }
                is Result.Error -> {
                    _errorEvent.emit(onDislikePost.message.toString())
                }
            }
        }
    }

    fun removePost(post: PostModel) {
        viewModelScope.launch {
            when (val deletePost = postRepository.deletePost(post.id!!)) {
                is Result.Success -> {
                    deletePost.data?.let {
                        _feed_post.value?.let {
                            it.remove(post)
                            _feed_post.postValue(it)
                        }
                        _my_post.value?.let {
                            it.remove(post)
                            _my_post.postValue(it)
                        }
                    }
                }
                is Result.Error -> {
                    _errorEvent.emit(deletePost.message.toString())
                }
            }
        }
    }

    private fun updateLikeState(post: PostModel) {
        _feed_post.value?.let { list ->
            list.find { it.id == post.id }?.let {
                it.userStatus = it.userStatus.copy(liked = !it.userStatus.liked)
                _feed_post.postValue(list)
            }
        }
        _popular_post.value?.let { list ->
            list.find { it.id == post.id }?.let {
                it.userStatus = it.userStatus.copy(liked = !it.userStatus.liked)
                _popular_post.postValue(list)
            }
        }
        _favorites_post.value?.let { list ->
            list.find { it.id == post.id }?.let {
                list.remove(it)
            } ?: run {
                list.add(
                    post.copy(
                        userStatus = PostMemberModel(
                            post.userStatus.member,
                            liked = true
                        )
                    )
                )
            }
            _favorites_post.postValue(list)
        }
        _my_post.value?.let { list ->
            list.find { it.id == post.id }?.let {
                it.userStatus = it.userStatus.copy(liked = !it.userStatus.liked)
                _my_post.postValue(list)
            }
        }
    }
}