package com.example.elogapp.repository.responses.dvir

import com.google.gson.annotations.SerializedName


data class NewDvirResponse(

        @SerializedName("status")
        val status: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("exceptionMessage")
        val exceptionMessage: String,
//        @SerializedName("data")
//        val data: Data
)
