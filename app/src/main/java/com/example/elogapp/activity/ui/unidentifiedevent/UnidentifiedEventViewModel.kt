package com.example.elogapp.activity.ui.unidentifiedevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.Outbound
import com.example.elogapp.database.UnidentifiedEvents
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.ExceptionRepository
import com.example.elogapp.repository.UnidentifiedDataRepository
import com.example.elogapp.repository.model.Exceptions
import com.example.elogapp.repository.responses.BaseResponse
import kotlinx.coroutines.*
import okhttp3.RequestBody

class UnidentifiedEventViewModel(private val repository: UnidentifiedDataRepository) : ViewModel() {



    fun getUserDetails (): LiveData<UserDetails> {

        return repository.getUser()
    }

    /**
     * Insert Outbound Data In Master
     */
    fun insertOutboundData(data: Outbound){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertOutboundData(data)
        }
    }

    fun getOutboundList(isSynced: Boolean): List<Outbound>{
        return repository.getOutboundList(isSynced)
    }

    fun deleteEvent(event: UnidentifiedEvents): Int{
        return repository.deleteEvent(event)
    }

    suspend fun sendEventDataToServerDispatcher(eventLog : RequestBody) : BaseResponse {
        return  repository.sendEventDataToServer(eventLog)
    }

    suspend fun checkedUnidentifiedEvent(id: Int, seqNo: Int, checked: Boolean, userId: Int) : Int {

        return  repository.checkedUnidentifiedEvent(id, seqNo, checked, userId)

    }

    fun getUnidentifiedEventsByStatus(checkedStatus: Boolean): List<UnidentifiedEvents> {
        return repository.getUnidentifiedEventsByStatus(checkedStatus)
    }

    fun getAllUnidentifiedEvents(): LiveData<List<UnidentifiedEvents>> {
        return repository.getAllUnidentifiedEvents()
    }


}