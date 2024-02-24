package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import com.example.elogapp.database.*
import com.example.elogapp.repository.model.ChartInfo
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.BaseResponse
import com.example.elogapp.repository.responses.master.*
import okhttp3.RequestBody

class HomeRepository(private val api: MyApi,
                     private val db: MyRoomDb) : SafeApiRequest() {

    /**
     * Download Master Data From Web..
     */
    suspend fun getMasterData(userId: Int, clientId: Int, key: String): MasterResponse {

        return apiRequest { api.getMasterData(userId, clientId, key) }

    }

    /**
     * Download Master Dropdown List
     */
    suspend fun getDropDownMasterData(userId: Int, clientId: Int, sessionKey: String, type: String): DropdownMasterResponse {

        return apiRequest { api.getMasterDropdownData(userId, clientId, sessionKey, type)

        }
    }

    /**
     * Save Driver Events
     */
    suspend fun saveDriverEventData(event: RequestBody): BaseResponse {

        return apiRequest { api.saveDriverEventNew(event)

        }
    }

    /**
     * Save Driver Events
     */
    suspend fun detMasterDropDownData(event: RequestBody): BaseResponse {

        return apiRequest { api.saveDriverEventNew(event)

        }
    }

    suspend fun sendEventDataToServer(event: RequestBody): BaseResponse {

        return apiRequest { api.sendEventDataToServer(event)

        }
    }

    /**
     * Insert Trailers List in Trailer Master Table
     */
    suspend fun insertDriverDutyMaster(driverDutyInfo: DriverDutyInfo){
        db.getDriverDutyDao().insertDuty(driverDutyInfo)
    }

    /**
     * Get Event Log List in Event Log Master Table
     */
     fun getEventLog() : EventLog{
        return db.getEventLogDao().getEventLogById()
    }

    /**
     * Get ALl Event By Type
     */
    suspend fun getAllEventByType(dutyId: Long, eventType : String) : List<EventLog>{
        return db.getEventLogDao().getAllEventByType(dutyId, eventType)
    }

    fun getAllEvents(isSynced: Boolean) : List<EventLog>{
        return db.getEventLogDao().getAllEvents(isSynced)
    }

    /**
     * Insert Event Log List in Event Log Master Table
     */
    fun insertEventLogMaster(eventLog: EventLog) {
        return db.getEventLogDao().insertEventLog(eventLog)
    }

    /**
     * Insert Violation in Violation Master Table
     */
    fun insertViolationInDb(violation: Violation) {
        return db.getViolationDao().insertViolation(violation)
    }


    fun updateDutyEndTimeAndDuration(eventType: String?,dutyId: Long?,endTime: String?, synced: Boolean){
        db.getEventLogDao().updateDutyEndTimeAndDuration(eventType, dutyId, endTime, synced)
    }



    fun getTotalEventsTime(eventType: String, dutyId: Long?):Int{
        return db.getEventLogDao().getTotalEventTime(eventType, dutyId)
    }

    /**
     * Insert Dropdown List in Dropdown Master Table
     */
    fun insertDropdownMaster(dropDownList: List<DropdownMaster>){
        db.getDropdownMasterDao().deleteAllDropdownList()
        db.getDropdownMasterDao().insertAll(dropDownList)
    }


    fun getChartData(date: String): MutableList<ChartInfo> {
        return db.getEventLogDao().getChartData()
    }

    /**
     * Get Trailer List from Trailer Master Table
     */
    fun getDropdownList (type: String): LiveData<List<DropdownMaster>>{

        return db.getDropdownMasterDao().getDropdownListByType(type)
    }


    fun getUserDetails (): LiveData<UserDetails> {

        return db.getUserDao().getUserDetails()
    }


    /**
     * Insert Outbound Data in Outbound Table
     */
    fun insertOutboundData(data: Outbound) : Long {
        return db.getOutboundDao().insertOutboundData(data)
    }

    fun insertOutboundData(data: UnidentifiedEvents) : Long {
        return db.getUnidentifiedEventDao().insertEventLog(data)
    }

    fun getOutboundData(): LiveData<List<Outbound>> {
        return db.getOutboundDao().getOutboundDataList()
    }

    fun getOutboundList(isSynced: Boolean): List<Outbound> {
        return db.getOutboundDao().getOutboundList(isSynced)
    }

    fun getUser() = db.getUserDao().getUser()

}