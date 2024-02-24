package com.example.elogapp.activity.ui.dalily_log

import com.google.gson.annotations.SerializedName

data class DailyLogData(


    @SerializedName("logDate") val date: String,
    @SerializedName("isCertified") val isCertified: Int,
    @SerializedName("image") val imageUrl: String

) {


}