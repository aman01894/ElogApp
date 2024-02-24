package com.example.elogapp.repository

import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.companyinfo.CompanyInfoResponse

class CompanyInfoRepository(
    private val api: MyApi,
    private val db: MyRoomDb
) : SafeApiRequest() {

    suspend fun getCustomerInfo(id: Int): CompanyInfoResponse {

        return apiRequest { api.getCustomerInfo(id) }

    }
}