package com.example.freshgrade.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.freshgrade.data.response.GetUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")


class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val TOKEN_KEY = stringPreferencesKey("token")


    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveUserData(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
        Log.d("UserPreference", "User data saved: $user")
    }

    suspend fun signIn() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences.clear()
        }
        clearToken()
    }

    suspend fun deleteAccount() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.remove(STATE_KEY)
            preferences.clear()
        }
        clearToken()
    }

    private suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null


        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

private operator fun Preferences.set(tokenKey: Preferences.Key<String>, value: String) {
    this[tokenKey] = value

}
