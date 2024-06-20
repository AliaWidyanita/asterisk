package com.dicoding.asterisk.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserDataStore private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[FULLNAME_KEY] = user.fullName
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                username = preferences[USERNAME_KEY] ?: "",
                fullName = preferences[FULLNAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLoggedIn = preferences[IS_LOGIN_KEY] ?: false
            )
        }

    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDataStore? = null
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val FULLNAME_KEY = stringPreferencesKey("fullName")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLoggedIn")

        fun getInstance(dataStore: DataStore<Preferences>): UserDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun getUserIdOnce(): String {
        return dataStore.data.map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }.first()
    }
}