package com.example.elogapp.repository

import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.master.MasterResponse
import com.example.elogapp.repository.responses.predvir.PreDvirTripResponse

class PreTripDvirRepository(private val api: MyApi,
                            private val db: MyRoomDb) : SafeApiRequest() {

    /**
     * Download DVIR Data From Web..
     */
    suspend fun getPreDvirList(userId: Int, clientId: Int, key: String): MasterResponse {

        return apiRequest { api.getMasterData(userId, clientId, key) }

    }

    /**
     * Save DVIR Events
     */
    suspend fun getPreDvirList(key: String): PreDvirTripResponse {

        return apiRequest { api.getPreDvirListData(key)

        }

    }



}