package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_ID = 0

@Entity(tableName = "OUT_BOUND")
data class Outbound @Ignore constructor(

    @ColumnInfo(name = "EVENT_TYPE")
    @SerializedName("eventType")
    var eventType: String,

    @ColumnInfo(name = "GSON_DATA")
    @SerializedName("gson")
    var data: String,

    @ColumnInfo(name = "SYNCED")
    @SerializedName("synced")
    var synced: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var ID: Int = CURRENT_ID //**do not made it val**


    constructor() : this("", "", false)
}




