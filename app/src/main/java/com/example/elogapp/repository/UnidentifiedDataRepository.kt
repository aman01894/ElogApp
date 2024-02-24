package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import com.example.elogapp.database.*
import com.example.elogapp.repository.model.Exceptions
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.BaseResponse
import okhttp3.RequestBody

class UnidentifiedDataRepository(private val api: MyApi,
                                 private val db: MyRoomDb) : SafeApiRequest() {


    suspend fun sendEventDataToServer(event: RequestBody): BaseResponse {

        return apiRequest { api.sendEventDataToServer(event)

        }
    }

    suspend fun checkedUnidentifiedEvent(id: Int,seqNo: Int, checked: Boolean, userId: Int) : Int {
        return  db.getUnidentifiedEventDao().updateUnidentifiedEventCheckedStatus(id, seqNo, checked, userId)
    }

    /**
     * Get Trailer List from Trailer Master Table
     */
    fun getUnidentifiedEventById(seqId: Int): LiveData<UnidentifiedEvents>{

        return db.getUnidentifiedEventDao().getUnidentifiedEventById(seqId)
    }

    fun getUnidentifiedEventsByStatus(checkedStatus: Boolean): List<UnidentifiedEvents>{

        return db.getUnidentifiedEventDao().getAllUnidentifiedEventsByStatus(checkedStatus)
    }

    fun getAllUnidentifiedEvents(): LiveData<List<UnidentifiedEvents>>{

        return db.getUnidentifiedEventDao().getAllUnidentifiedEvents()
    }

    fun deleteEvent(event: UnidentifiedEvents): Int{

        return db.getUnidentifiedEventDao().delete(event)
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