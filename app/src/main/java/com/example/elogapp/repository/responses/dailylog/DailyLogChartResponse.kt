package com.example.elogapp.repository.responses.dailylog

import Data
import com.google.gson.annotations.SerializedName

data class DailyLogChartResponse(
    @SerializedName("status") val status : String,
    @SerializedName("message") val message : String,
    @SerializedName("exceptionMessage") val exceptionMessage : String,
    @SerializedName("data") val data : Data
)