package com.example.elogapp.repository.responses.companyinfo

import com.example.elogapp.repository.model.CompanyInfoData
import com.google.gson.annotations.SerializedName


data class CompanyInfoResponse(

        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("exceptionMessage")
        val exceptionMessage: String,
        @SerializedName("data")
        val data: CompanyInfoData
)
