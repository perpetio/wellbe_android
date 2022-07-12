package com.perpetio.well_be.ui.main.viewmodel

import androidx.lifecycle.*
import com.perpetio.well_be.data.AccountModule
import com.perpetio.well_be.dto.message.MessageModel
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.repo.chat.ChatRepository
import com.perpetio.well_be.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val accManager: AccountModule
) : ViewModel(), DefaultLifecycleObserver {

    private val _chats: MutableLiveData<MutableList<PostModel>> = MutableLiveData()
    val chats: LiveData<MutableList<PostModel>> get() = _chats

    private val _messages: MutableLiveData<MutableList<MessageModel>> = MutableLiveData()
    val messages: LiveData<MutableList<MessageModel>> get() = _messages

    private val _sentMessageEvent = MutableSharedFlow<Unit>()
    val sentMessageEvent = _sentMessageEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _joinLeftEvent = MutableSharedFlow<Boolean>()
    val joinLeftEvent = _joinLeftEvent.asSharedFlow()

    val userId: Long get() = accManager.getUserId()

    fun getAllChatsList() {
        viewModelScope.launch {
            when (val chatList = chatRepository.getAllChats()) {
                is Result.Success -> {
                    _chats.postValue(chatList.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(chatList.message.toString())
                }
            }
        }
    }

    fun getAllMessagesForRoom(roomId: Long) {
        viewModelScope.launch {
            when (val allRoomMessages = chatRepository.getAllRoomMessages(roomId)) {
                is Result.Success -> {
                    _messages.postValue(allRoomMessages.data?.results?.toMutableList())
                }
                is Result.Error -> {
                    _errorEvent.emit(allRoomMessages.message.toString())
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(message)
            _sentMessageEvent.emit(Unit)
        }
    }

    fun initSession(currentRoom: Long) {
        viewModelScope.launch {
            when (val initSession = chatRepository.initSession(currentRoom)) {
                is Result.Success -> {
                    if (initSession.data == true) {
                        chatRepository.observeIncomingMessages().onEach { messageModel ->
                            _messages.value?.let {
                                it.add(messageModel)
                                _messages.postValue(it)
                            }
                        }.launchIn(viewModelScope)
                    }
                }
                is Result.Error -> {
                    _errorEvent.emit(initSession.message.toString())
                }
            }
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            chatRepository.closeSession()
        }
    }

    fun onJoinChat(roomId: Long, currentState: Boolean) {
        viewModelScope.launch {
            val response: Result<Boolean> = if (currentState) {
                chatRepository.removeUserFromRoom(roomId)
            } else chatRepository.addUserToRoom(roomId)
            when (response) {
                is Result.Success -> {
                    _joinLeftEvent.emit(!currentState)
                }
                is Result.Error -> {
                    _errorEvent.emit(response.message.toString())
                }
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        _messages.value?.clear()
    }

}