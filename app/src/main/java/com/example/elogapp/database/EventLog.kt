package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

const val CURRENT_TIME_ID = 0

@Entity(tableName = "EVENT_TIME_LOG")
data class EventLog @Ignore constructor(


    @ColumnInfo(name = "DUTY_ID")
    @SerializedName("dutyId")
    var dutyId: Long,


    @ColumnInfo(name = "CLIENT_ID")
    @SerializedName("clientId")
    var clientId: Int,

    //Driver ID
    @ColumnInfo(name = "USER_ID")
    @SerializedName("userId")
    var userId: Int,

    @ColumnInfo(name = "CO_DRIVER_ID")
    @SerialName("coDriverId")
    var coDriverId: Int,


    @ColumnInfo(name = "VEHICLE_ID")
    @SerializedName("vehicleId")
    var vehicleId: Int,


    @ColumnInfo(name = "TRAILER_ID")
    @SerialName("trailerId")
    var trailerId: Int,

    @ColumnInfo(name = "CURRENT_STATUS")
    @SerializedName("currentStatus")
    var currentStatus: String,

    @ColumnInfo(name = "EVENT_TYPE")
    @SerializedName("eventTypeName")
    var eventType: String,

    @ColumnInfo(name = "EVENT_TYPE_ID")
    @SerializedName("eventType")
    var eventTypeId: Int,

    @ColumnInfo(name = "CURRENT_LOCATION")
    @SerialName("location")
    var location: String,

    @ColumnInfo(name = "CUSTOM_LOCATION")
    @SerialName("customLocation")
    var customLocation: String,

    @ColumnInfo(name = "CURRENT_ODOMETER")
    @SerialName("odoMeter")
    var odoMeter: String,

    @ColumnInfo(name = "NOTES")
    @SerialName("notes")
    var notes: String,

    //Event Time Start
    @ColumnInfo(name = "TIME_STAMP")
    @SerializedName("eventTime")
    var eventTime: String?,

    //Event Time End
    @ColumnInfo(name = "EVENT_END_TIME")
    @SerializedName("eventEndTime")
    var eventEndTime: String?,

    //Event Duration
    @ColumnInfo(name = "EVENT_DURATION")
    @SerializedName("duration")
    var eventDuration: String?,

    @JsonIgnore
    @JsonProperty(value = "SYNCED")
    @ColumnInfo(name = "SYNCED")
    @SerializedName("synced")
    var synced: Boolean

) {
    @JsonIgnore
    @JsonProperty(value = "ID")
    @PrimaryKey(autoGenerate = true)
    var ID: Int = CURRENT_TIME_ID //**do not made it val**

    constructor() : this(-1, -1, -1,-1,-1,-1,"", "",-1, "", "", "", "","","","",false)




}




