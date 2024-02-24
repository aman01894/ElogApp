package com.example.elogapp.repository

import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.load.LoadAcceptRejectResponse
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import okhttp3.RequestBody

class LoadRepository(private val api: MyApi,
                     private val db: MyRoomDb
) : SafeApiRequest()  {

    suspend fun getDispatchData(status: Int): LoadDispatchResponse {

        return apiRequest { api.getLoadData(status)}

    }

    suspend fun getOpenLoadData(status: Int): LoadDispatchResponse {

        return apiRequest { api.getOpenLoadData(status)}

    }

    suspend fun getPaymentHistoryData(fromDate: String, toDate: String): LoadDispatchResponse {

        return apiRequest { api.getPaymentHistoryData(fromDate, toDate)}

    }

    suspend fun getDocLoads(fromDates: String, toDates: String): LoadDispatchResponse {

        return apiRequest { api.getDocLoads(fromDates,toDates)}

    }


    suspend fun saveLoadAcceptReject(event: RequestBody): LoadAcceptRejectResponse {

        return apiRequest { api.updateAcceptRejectStatus(event)}

    }

    suspend fun updateLoadDetailStatus(event: RequestBody): LoadAcceptRejectResponse {

        return apiRequest { api.updateLoadDetailStatus(event)}

    }


}