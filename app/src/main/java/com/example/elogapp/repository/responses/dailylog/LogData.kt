package com.example.elogapp.repository.responses.dailylog

import com.google.gson.annotations.SerializedName

data class LogData(

    @SerializedName("date") val date : String,
    @SerializedName("isCertified") val isCertified : Int,
    @SerializedName("imageUrl") val imageUrl : String
    )
