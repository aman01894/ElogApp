package com.example.elogapp.repository.responses.predvir

import com.example.elogapp.repository.model.DvirPreTripData
import com.google.gson.annotations.SerializedName


data class PreDvirTripResponse(

        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("exceptionMessage")
        val exceptionMessage: String,
        @SerializedName("data")
        val data: List<DvirPreTripData>
)
