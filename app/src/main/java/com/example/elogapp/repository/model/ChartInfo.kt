package com.example.elogapp.repository.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class ChartInfo(


    @ColumnInfo(name = "EVENT_TYPE")
    var label: String,

    @ColumnInfo(name = "ID")
    var id: Int,

    @Ignore
    @SerializedName("lineColor")
    var lineColor: String?,

    @SerializedName("x")
    @ColumnInfo(name = "TIME_STAMP")
    var x: String,

    @ColumnInfo(name = "DT")
    var date: String,

    @ColumnInfo(name = "EVENT_END_TIME")
    var event_end_time: String,

    @Ignore
    @SerializedName("y")
    var y: Int

) {

    constructor() : this (
        "",0,"","","","",15,)
}
