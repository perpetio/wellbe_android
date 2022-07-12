package com.perpetio.well_be.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.perpetio.well_be.dto.user.UserModel
import com.perpetio.well_be.dto.user.UserUpdatingModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStoreManager(val context: Context) {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")

    companion object {
        val ID = longPreferencesKey("ID")
        val USERNAME = stringPreferencesKey("USERNAME")
        val MAIL = stringPreferencesKey("MAIL")
        val COUNTRY = stringPreferencesKey("COUNTRY")
        val CITY = stringPreferencesKey("CITY")
        val TOKEN = stringPreferencesKey("TOKEN")
    }

    suspend fun saveUserToDataStore(user: UserModel) {
        context.userDataStore.edit {
            it[ID] = user.id
            it[USERNAME] = user.username
            it[MAIL] = user.mail ?: "Not specified"
            it[COUNTRY] = user.country ?: "Not specified"
            it[CITY] = user.city ?: "Not specified"
        }
    }

    suspend fun updateUser(user: UserUpdatingModel) {
        context.userDataStore.edit {
            it[USERNAME] = user.username ?: "Not specified"
            it[COUNTRY] = user.country ?: "Not specified"
            it[CITY] = user.city ?: "Not specified"
        }
    }

    fun getUserFromDataStore(): Flow<UserModel> {
        return context.userDataStore.data.map {
            UserModel(
                id = it[ID] ?: -1,
                username = it[USERNAME] ?: "Not specified",
                mail = it[MAIL] ?: "Not specified",
                country = it[COUNTRY] ?: "Not specified",
                city = it[CITY] ?: "Not specified"
            )
        }
    }

    suspend fun saveUserToken(token: String) {
        context.userDataStore.edit {
            it[TOKEN] = token
        }
    }

    fun getUserToken(): String {
        return runBlocking {
            context.userDataStore.data.map {
                it[TOKEN] ?: ""
            }.first()
        }
    }

    fun getUserId(): Long {
        return runBlocking {
            context.userDataStore.data.map {
                it[ID] ?: 0L
            }.first()
        }
    }

    suspend fun clearData() {
        context.userDataStore.edit {
            it.clear()
        }
    }

}

