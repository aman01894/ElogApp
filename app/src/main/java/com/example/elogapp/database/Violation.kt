package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

const val VIOLATION_ID = 0
//{
//    "EventType": "VIOLATION",
//    "EventData": {
//    "EventDateTime": 20230122123240,
//    "userID": 4,
//    "vehicleID": 11,
//    "violationType": "120",
//    "violationDateTime": "2023-01-22 07:17:16",
//    "synced": false
//}
//}
@Entity(tableName = "VIOLATION_TABLE")
data class Violation @Ignore constructor(

    @ColumnInfo(name = "DUTY_ID")
    @SerialName("dutyId")
    var dutyId: Long,

    @ColumnInfo(name = "DRIVER_ID")
    @SerialName("userId")
    var userId: Int,

    @ColumnInfo(name = "VEHICLE_ID")
    @SerialName("vehicleId")
    var vehicleId: Int,

    @ColumnInfo(name = "VIOLATION_TYPE")
    @SerializedName("violationType")
    var violationType: String,

    @ColumnInfo(name = "VIOLATION_DATE_TIME")
    @SerializedName("violationDateTime")
    var violationTime: String,


    @ColumnInfo(name = "SYNCED")
    @SerializedName("synced")
    var synced: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var ID: Int = VIOLATION_ID //**do not made it val**


    constructor() : this(-1, 0,0, "","", false)
}




