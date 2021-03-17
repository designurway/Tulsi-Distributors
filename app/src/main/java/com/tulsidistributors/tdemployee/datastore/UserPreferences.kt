package com.tulsidistributors.tdemployee.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {

    private val applicationContext = context.applicationContext

    companion object {
        val EMP_ID = preferencesKey<String>("empId")
        val EMP_FIRST_NAME = preferencesKey<String>("empFname")
        val EMP_EMAIL = preferencesKey<String>("empEmail")
    }

    // creating datastore instance
    private val dataStore: DataStore<Preferences>

    init {
        //inatilizing dataStore Instance
        dataStore = applicationContext.createDataStore(
            // name for our dataStore
            name = "user_login_datastore"
        )
    }

    suspend fun saveUserLoginDetail(empId: String, first_name: String, email: String) {

        dataStore.edit { preferences ->
            preferences[EMP_ID] = empId
            preferences[EMP_EMAIL] = email
            preferences[EMP_FIRST_NAME] = first_name

        }
    }

    val empIdFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[EMP_ID]
        }

    val empEmailFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[EMP_EMAIL]
        }
}