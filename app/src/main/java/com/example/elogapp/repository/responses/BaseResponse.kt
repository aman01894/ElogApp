package com.example.elogapp.repository.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("exceptionMessage") val exceptionMessage: String
)