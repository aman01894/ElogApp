package com.example.elogapp.repository.responses.dailylog

import com.example.elogapp.activity.ui.dalily_log.DailyLogData
import com.google.gson.annotations.SerializedName

data class DailyLogResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("exceptionMessage") val exceptionMessage: String,
    @SerializedName("data") val data: List<DailyLogData>
)