package com.perpetio.well_be.repo.chat

import com.perpetio.well_be.dto.message.MessageModel
import com.perpetio.well_be.dto.message.MessageResponseModel
import com.perpetio.well_be.dto.post.PostResponseModel
import com.perpetio.well_be.network.FormattedNetworkClientException
import com.perpetio.well_be.network.WebSocketBuilder
import com.perpetio.well_be.utils.Result
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatRepositoryImpl(private val client: WebSocketBuilder) : ChatRepository {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(room: Long): Result<Boolean> {
        return try {
            socket = client.socket.webSocketSession {
                url(ChatRepository.Endpoints.CHAT_SOCKET.url + "/$room")
            }
            Result.Success(socket?.isActive == true)
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(message = exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun observeIncomingMessages(): Flow<MessageModel> {
        return try {
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    Json.decodeFromString(json)
                } ?: flow { }
        } catch (ex: Exception) {
            flow { }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    override suspend fun getAllRoomMessages(roomId: Long): Result<MessageResponseModel> {
        return try {
            Result.Success(
                client.socket.get(ChatRepository.Endpoints.GET_ALL_ROOM_MESSAGES.url + "/$roomId")
                    .body()
            )
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun getAllChats(): Result<PostResponseModel> {
        return try {
            Result.Success(
                client.socket.get(ChatRepository.Endpoints.GET_ALL_ROOMS.url)
                    .body()
            )
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun addUserToRoom(roomId: Long): Result<Boolean> {
        return try {
            Result.Success(
                client.socket.post(ChatRepository.Endpoints.JOIN_LEAVE_ROOM.url + "/$roomId")
                    .body()
            )
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }

    override suspend fun removeUserFromRoom(roomId: Long): Result<Boolean> {
        return try {
            Result.Success(
                client.socket.delete(ChatRepository.Endpoints.JOIN_LEAVE_ROOM.url + "/$roomId")
                    .body()
            )
        } catch (exception: FormattedNetworkClientException) {
            Result.Error(exception.formattedErrorMessage)
        } catch (exception: Exception) {
            Result.Error("Server or network error")
        }
    }
}