package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import com.example.elogapp.database.*
import com.example.elogapp.repository.model.Exceptions
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.BaseResponse
import okhttp3.RequestBody

class ExceptionRepository(private val api: MyApi,
                          private val db: MyRoomDb) : SafeApiRequest() {


    suspend fun sendEventDataToServer(event: RequestBody): BaseResponse {

        return apiRequest { api.sendEventDataToServer(event)

        }
    }

    /**
     * Get Trailer List from Trailer Master Table
     */
    fun getExceptionList (type: String): LiveData<List<Exceptions>>{

        return db.getDropdownMasterDao().getExceptionList(type)
    }


    fun getUserDetails (): LiveData<UserDetails> {

        return db.getUserDao().getUserDetails()
    }


    /**
     * Insert Outbound Data in Outbound Table
     */
    fun insertOutboundData(data: Outbound) : Long{
        return db.getOutboundDao().insertOutboundData(data)
    }

    fun getOutboundData(): LiveData<List<Outbound>> {
        return db.getOutboundDao().getOutboundDataList()
    }

    fun getOutboundList(isSynced: Boolean): List<Outbound> {
        return db.getOutboundDao().getOutboundList(isSynced)
    }

    fun getUser() = db.getUserDao().getUser()

}