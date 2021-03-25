package com.tulsidistributors.tdemployee.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLoginPreferences(val mDataStore: DataStore<Preferences>) {


    /*private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_PREFERENCES_NAME)
    private val mDataStore: DataStore<Preferences> = context.dataStore*/

    companion object {
        val EMP_ID = stringPreferencesKey("empId")
        val DISTRIBUTOR_ID = stringPreferencesKey("distributor_id")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone_number")
        val ROLE = stringPreferencesKey("role")


    }

    suspend fun saveUserLoginDetail(
        empId: String,
        distributor_id: String,
        email: String,
        phone: String,
        role:String
    ) {

        mDataStore.edit { preferences ->
            preferences[EMP_ID] = empId
            preferences[DISTRIBUTOR_ID] = distributor_id
            preferences[EMAIL] = email
            preferences[PHONE] = phone
            preferences[ROLE] = role


        }

    }

    suspend fun logout(){
        mDataStore.edit {
            it.clear()
        }
    }

    val empIdFlow: Flow<String?> = mDataStore.data.map { preferences ->
        preferences[EMP_ID]

    }

    val distributorIdFlow:Flow<String?> = mDataStore.data.map {
            it[DISTRIBUTOR_ID]
    }

    val emailFlow:Flow<String?> = mDataStore.data.map { preferences->
        preferences[EMAIL]
    }
}

