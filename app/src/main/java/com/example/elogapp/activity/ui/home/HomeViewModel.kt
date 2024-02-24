package com.example.elogapp.activity.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.elogapp.database.*
import com.example.elogapp.util.pref.UserPreference
import com.example.elogapp.repository.HomeRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.NoInternetException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.RequestBody

import com.example.elogapp.repository.model.ChartInfo
import com.example.elogapp.repository.responses.BaseResponse


class HomeViewModel(private var repository: HomeRepository) : ViewModel() {

    var downloadListener: MasterDownloadListener? = null
    var userId: String? = null
    var clientId: String? = null

    fun getUserDetail() = repository.getUser()


    fun downloadDropDownMaster(userId: Int, clientId: Int, sessionKey: String, type: String) {

        Log.d(AppConstants.TAG, "Download Master Called")
        viewModelScope.launch {

            try {
                val response = repository.getDropDownMasterData(userId, clientId, sessionKey, type)
                downloadListener?.onSuccess(response)
            } catch (e: NoInternetException) {
                e.printStackTrace()
                downloadListener?.onFailure(e.message!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                downloadListener?.onFailure(e.message!!)
            }
        }
    }

    fun getChartData(date: String) : MutableList<ChartInfo>{
        return repository.getChartData(date)
    }


    fun insertDropdownMaster(dropdownList: List<DropdownMaster>){
        repository.insertDropdownMaster(dropdownList)

    }


    /**
     * Insert Outbound Data In Master
     */
    fun insertOutboundData(data: Outbound){
        viewModelScope.launch(Dispatchers.IO){
            val id = repository.insertOutboundData(data)
            Log.d("INSERT ID", "INSERT ID: $id")
        }
    }

    fun insertUnidentifiedEvents(data: UnidentifiedEvents){
        viewModelScope.launch(Dispatchers.IO){
            val id = repository.insertOutboundData(data)
            Log.d("INSERT ID", "INSERT ID: $id")
        }
    }


    fun getOutboundList(isSynced: Boolean): List<Outbound>{
        return repository.getOutboundList(isSynced)
    }

    fun saveDutyIdPref(context: UserPreference, dutyId: String){

        viewModelScope.async {

            context.saveDutyId(dutyId)
        }
    }


    fun saveDutyStartTimePref(context: UserPreference, dutyTime: String){

        viewModelScope.launch {

            context.saveStartDutyTime(dutyTime)
        }
    }


    fun saveDriverInfoInDB(info: DriverDutyInfo){

        viewModelScope.launch(Dispatchers.IO) {

            repository.insertDriverDutyMaster(info)
        }
    }

     fun getEventLogFromDb() : EventLog{

        return repository.getEventLog()

    }

    suspend fun getAllEventByType(dutyId: Long, eventType : String) : List<EventLog>{

        return repository.getAllEventByType(dutyId, eventType)

    }

    fun getAllEvents(isSynced : Boolean) : List<EventLog>{

        return repository.getAllEvents(isSynced)

    }

    fun saveEventLogInDb(info: EventLog){

        CoroutineScope(Dispatchers.IO).launch {

            repository.insertEventLogMaster(info)
        }
    }

    fun insertViolationInDb(violation: Violation){

        CoroutineScope(Dispatchers.IO).launch {

            repository.insertViolationInDb(violation)
        }
    }



    fun updateDutyEndTimeAndDuration(eventType: String?, dutyId: Long?,eventEndTime: String?, synced: Boolean,){

        CoroutineScope(Dispatchers.IO).launch {

            repository.updateDutyEndTimeAndDuration(eventType, dutyId, eventEndTime, synced)
        }
    }




    fun getDropdownList (type: String): LiveData<List<DropdownMaster>> {

        return repository.getDropdownList(type)
    }

    fun getUserDetails (): LiveData<UserDetails> {

        return repository.getUserDetails()
    }

    fun getTotalEventsTime(eventType: String, dutyId: Long?):Int{

        return repository.getTotalEventsTime(eventType, dutyId)
    }


    suspend fun sendOutBondDataToServer(eventLog : RequestBody) : BaseResponse{

        return  repository.sendEventDataToServer(eventLog)

    }


}