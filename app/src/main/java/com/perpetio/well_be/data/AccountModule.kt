package com.perpetio.well_be.data

import com.perpetio.well_be.dto.user.UserCredModel
import com.perpetio.well_be.dto.user.UserModel
import com.perpetio.well_be.dto.user.UserUpdatingModel
import kotlinx.coroutines.flow.Flow

class AccountModule(private val dataStoreManager: DataStoreManager) {

    suspend fun saveUser(user: UserCredModel) {
        dataStoreManager.saveUserToDataStore(
            UserModel(
                user.id,
                user.username,
                user.mail,
                user.country,
                user.city
            )
        )
        user.token?.let { saveToken(it) }
    }

    fun getUser(): Flow<UserModel> {
        return dataStoreManager.getUserFromDataStore()
    }

    suspend fun saveToken(token: String) {
        dataStoreManager.saveUserToken(token)
    }

    fun getToken(): String {
        return dataStoreManager.getUserToken()
    }

    suspend fun updateUser(user: UserUpdatingModel) {
        dataStoreManager.updateUser(user)
    }

    fun getUserId(): Long {
        return dataStoreManager.getUserId()
    }

    suspend fun clearData() {
        dataStoreManager.clearData()
    }
}