package com.example.elogapp.util.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreference(context: Context) {



    companion object {

        private val Context.dataStore by preferencesDataStore("elog_app_preferences")

        private val KEY_VERSION = stringPreferencesKey("VERSION_")

        private val KEY_IS_MASTER_SYNCED = stringPreferencesKey("IS_SYNCED_")

        private val KEY_DUTY_ID = stringPreferencesKey("DUTY_ID_");

        private val KEY_CURRENT_ID = intPreferencesKey("CURRENT_ID_");
        private val KEY_CURRENT_TIME_STAMP = stringPreferencesKey("CURRENT_TIME_STAMP_");
        private val KEY_CURRENT_ACTIVITY = stringPreferencesKey("CURRENT_ACTIVITY_");

        private val KEY_START_DUTY_TIME = stringPreferencesKey("START_TIME_DUTY_");

        private val KEY_DRIVER_ID = intPreferencesKey("DRIVER_ID_");
        private val KEY_DRIVER_NAME = stringPreferencesKey("DRIVER_NAME_");
        private val KEY_CO_DRIVER_ID = intPreferencesKey("CO_DRIVER_ID_");
        private val KEY_CO_DRIVER_NAME = stringPreferencesKey("CO_DRIVER_NAME_");
        private val KEY_VEHICLE_ID = intPreferencesKey("VEHICLE_ID_");
        private val KEY_VEHICLE_NO = stringPreferencesKey("VEHICLE_NO_");
        private val KEY_TRAILER_ID = intPreferencesKey("TRAILER_ID_");
        private val KEY_TRAILER_NO = stringPreferencesKey("TRAILER_NO_");
        private val KEY_CLIENT_ID = intPreferencesKey("CLIENT_ID_");
        private val KEY_PARENT_ID = stringPreferencesKey("PARENT_ID");
        private val KEY_AUTH = stringPreferencesKey("AUTH");
        private val KEY_SLEEP_SPLIT = intPreferencesKey("SLEEP_SPLIT_");
    }

    private val dataStore: DataStore<Preferences> = context.dataStore


    val isSynced: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_IS_MASTER_SYNCED]
        }


    suspend fun setMasterSynced(flag: String) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_MASTER_SYNCED] = flag
        }
    }


    val dutyId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_DUTY_ID]
        }

    suspend fun saveDutyId(id: String) {
        dataStore.edit { preferences ->
            preferences[KEY_DUTY_ID] = id
        }
    }

    val dutyStartTime: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_START_DUTY_TIME]
        }


    suspend fun saveStartDutyTime(dutyTime: String) {
        dataStore.edit { preference ->
            preference[KEY_START_DUTY_TIME] = dutyTime
        }
    }

    /**
     * Save & Get Driver Id
     */

    val driverId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_DRIVER_ID]
        }

    suspend fun saveDriverId(driverId: Int) {
        dataStore.edit { preference ->
            preference[KEY_DRIVER_ID] = driverId
        }
    }


    /**
     * Save & Get Split Id
     */

    val sleepSplit: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_SLEEP_SPLIT]
        }

    suspend fun sleepSplit(sleepSplit: Int) {
        dataStore.edit { preference ->
            preference[KEY_SLEEP_SPLIT] = sleepSplit
        }
    }


    /**
     * Save & Get Driver NAME
     */

    suspend fun saveCoDriverName(name: String) {
        dataStore.edit { preferences ->
            preferences[KEY_CO_DRIVER_NAME] = name
        }
    }

    val driverName: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CO_DRIVER_NAME]
        }

    /**
     * Save & Get Co-Driver Id
     */

    val coDriverId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CO_DRIVER_ID]
        }

    suspend fun saveCoDriverId(coDriverId: Int) {
        dataStore.edit { preference ->
            preference[KEY_CO_DRIVER_ID] = coDriverId
        }
    }

    /**
     * Save & Get Vehicle Id
     */
    val truckId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_VEHICLE_ID]
        }

    suspend fun saveTruckId(vehicleId: Int) {
        dataStore.edit { preference ->
            preference[KEY_VEHICLE_ID] = vehicleId
        }
    }

    /**
     * Save & Get Vehicle No
     */
    val truckNumber: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_VEHICLE_NO]
        }

    suspend fun saveTruckNo(vehicleNo: String) {
        dataStore.edit { preference ->
            preference[KEY_VEHICLE_NO] = vehicleNo
        }
    }

    /**
     * Save & Get Trailer Id
     */
    val trailerId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_TRAILER_ID]
        }

    suspend fun saveTrailerId(trailerId: Int) {
        dataStore.edit { preference ->
            preference[KEY_TRAILER_ID] = trailerId
        }
    }

    /**
     * Save & Get Trailer No
     */
    val trailerNo: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_TRAILER_NO]
        }

    suspend fun saveTrailerNo(trailerNo: String) {
        dataStore.edit { preference ->
            preference[KEY_TRAILER_NO] = trailerNo
        }
    }



    /**
     * Save & Get Client Id
     */
    val clientId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CLIENT_ID]
        }

    suspend fun saveClientId(clientId: Int) {
        dataStore.edit { preference ->
            preference[KEY_CLIENT_ID] = clientId
        }
    }

    /**
     * Save & Get Current Id
     */
    val currentId: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CURRENT_ID]
        }

    suspend fun saveCurrentId(currentId: Int) {
        dataStore.edit { preference ->
            preference[KEY_CURRENT_ID] = currentId
        }
    }

    /**
     * Save & Get Current Activity Start Time
     */
    val currentTimeStamp: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CURRENT_TIME_STAMP]
        }

    suspend fun saveCurrentTimeStamp(currentId: String) {
        dataStore.edit { preference ->
            preference[KEY_CURRENT_TIME_STAMP] = currentId
        }
    }

    /**
     * Save & Get Current Activity
     */
    val currentActivity: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_CURRENT_ACTIVITY]
        }

    suspend fun saveCurrentActivity(currentActivity: String) {
        dataStore.edit { preference ->
            preference[KEY_CURRENT_ACTIVITY] = currentActivity
        }
    }

    /**
     * Save & Get Parent ID
     */
    val parentId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_PARENT_ID]
        }

    suspend fun saveParentId(parentId: String) {
        dataStore.edit { preference ->
            preference[KEY_PARENT_ID] = parentId
        }
    }

    /**
     * Save & Get Parent ID
     */
    val authKey: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    suspend fun saveAuthKey(auth: String) {
        dataStore.edit { preference ->
            preference[KEY_AUTH] = auth
        }
    }


}

