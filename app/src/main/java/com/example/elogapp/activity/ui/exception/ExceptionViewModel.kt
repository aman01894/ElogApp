package com.example.elogapp.activity.ui.exception

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.Outbound
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.ExceptionRepository
import com.example.elogapp.repository.model.Exceptions
import com.example.elogapp.repository.responses.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ExceptionViewModel(private val repository: ExceptionRepository) : ViewModel() {

    fun getDropdownList (type: String): LiveData<List<Exceptions>> {

        return repository.getExceptionList(type)
    }

    fun getUserDetails (): LiveData<UserDetails> {

        return repository.getUserDetails()
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

    suspend fun sendEventDataToServerDispatcher(eventLog : RequestBody) : BaseResponse {

        return  repository.sendEventDataToServer(eventLog)

    }


}