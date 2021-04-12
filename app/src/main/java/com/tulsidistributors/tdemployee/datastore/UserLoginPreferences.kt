package com.tulsidistributors.tdemployee.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLoginPreferences(val mDataStore: DataStore<Preferences>) {


    /*private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_PREFERENCES_NAME)
    private val mDataStore: DataStore<Preferences> = context.dataStore*/

    companion object {
        val EMP_ID = stringPreferencesKey("empId")
        val SaleExecutive_ID = stringPreferencesKey("SaleExecutive_ID")
        val DISTRIBUTOR_ID = stringPreferencesKey("distributor_id")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone_number")
        val ROLE = stringPreferencesKey("role")
        val BRAND_NAME = stringPreferencesKey("brand_name")
        val BRAND_ID = stringPreferencesKey("brand_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LOGGED_FIRST_TIME = booleanPreferencesKey("loggedInFirstTime")
        val PERMISSION = booleanPreferencesKey("permission")


    }


    suspend fun saveIsLoggedIn(
        isLoggedIn:Boolean
    ){
        mDataStore.edit {  preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun checkPremission(
        permission: Boolean
    ){
        mDataStore.edit {  preferences ->
            preferences[PERMISSION] = permission
        }
    }

    suspend fun saveUserLoginDetail(
        sale_executive_id: String,
        empId: String,
        distributor_id: String,
        email: String,
        phone: String,
        role: String,
        brand_name: String,
        brand_id: String,
        loggedInFirstTime:Boolean
    ) {

        mDataStore.edit { preferences ->
            preferences[SaleExecutive_ID] = sale_executive_id
            preferences[EMP_ID] = empId
            preferences[DISTRIBUTOR_ID] = distributor_id
            preferences[EMAIL] = email
            preferences[PHONE] = phone
            preferences[ROLE] = role
            preferences[BRAND_NAME] = brand_name
            preferences[BRAND_ID] = brand_id
            preferences[LOGGED_FIRST_TIME] = loggedInFirstTime

        }

    }

    suspend fun logout() {
        mDataStore.edit {
            it.clear()
        }
    }

    val saleExecutiveIdFlow: Flow<String?> = mDataStore.data.map {
        it[SaleExecutive_ID]
    }

    val empIdFlow: Flow<String?> = mDataStore.data.map { preferences ->
        preferences[EMP_ID]

    }

    val distributorIdFlow: Flow<String?> = mDataStore.data.map {
        it[DISTRIBUTOR_ID]
    }

    val emailFlow: Flow<String?> = mDataStore.data.map { preferences ->
        preferences[EMAIL]
    }

    val brandNameFlow: Flow<String?> = mDataStore.data.map { preferences ->
        preferences[BRAND_NAME]
    }

    val brandIdFlow:Flow<String?> = mDataStore.data.map {  preferences ->
        preferences[BRAND_ID]
    }

    val phoneFlow:Flow<String?> = mDataStore.data.map {  preferences ->
        preferences[PHONE]
    }

    val isLoggedIn:Flow<Boolean?> = mDataStore.data.map {  preferences ->
        preferences[IS_LOGGED_IN]
    }

    val loggedInFirstTime:Flow<Boolean?> = mDataStore.data.map {  preferences ->
        preferences[LOGGED_FIRST_TIME]
    }
}

