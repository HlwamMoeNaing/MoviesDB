package com.hmn.data.movies_data.local

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MyDataStore @Inject constructor(
    private val application: Application
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("myDataStore")

        val USER_NAME = stringPreferencesKey("user_name")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = intPreferencesKey("password")
        val IS_LOGIN = booleanPreferencesKey("is_login")


    }

    suspend fun setUserName(string: String) {
        application.dataStore.edit { preferences ->
            preferences[USER_NAME] = string
        }
    }

    val getUserName: Flow<String> = application.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    suspend fun setEmail(email: String) {
        application.dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    val getEmail: Flow<String> = application.dataStore.data.map { preferences ->
        preferences[EMAIL] ?: ""
    }


    suspend fun setPassword(password: Int) {
        application.dataStore.edit { preferences ->
            preferences[PASSWORD] = password
        }
    }

    val getPassword: Flow<Int> = application.dataStore.data.map { preferences ->
        preferences[PASSWORD] ?: 0
    }

    suspend fun setLoginStatus(shouldAllow: Boolean) {
        application.dataStore.edit { preferences ->
            preferences[IS_LOGIN] = shouldAllow
        }
    }

    val getLoginStatus = application.dataStore.data.map { preferences ->
        return@map preferences[IS_LOGIN] ?: false
    }

}