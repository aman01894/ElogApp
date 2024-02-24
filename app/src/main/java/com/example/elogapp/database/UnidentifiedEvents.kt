package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

const val UN_IDENTIFIED_EVeNT_ID = 0

@Entity(tableName = "UNIDENTIFIED_EVENTS")
data class UnidentifiedEvents @Ignore constructor(

    @ColumnInfo(name = "User_Id")
    @SerializedName("userId")
    var userId: String?,

    @ColumnInfo(name = "Vehicle_Id")
    @SerializedName("vehicleId")
    var vehicleId: Int,

    @ColumnInfo(name = "VIN")
    @SerializedName("vin")
    var vin: String,

    @ColumnInfo(name = "IGNITION_STATUS")
    @SerializedName("ignition_status")
    var ignitionStatus: String,

    @ColumnInfo(name = "EVENT_TIME")
    @SerializedName("eventTime")
    var eventTime: String?,

    @ColumnInfo(name = "EVENT_TIME_DEVICE")
    @SerializedName("eventTime")
    var eventTimeDevice: String?,

    @ColumnInfo(name = "ENGINE_AGE")
    @SerializedName("engineAge")
    var engineAge: String,

    @ColumnInfo(name = "ENGINE_HOURS")
    @SerializedName("engineHours")
    var engineHours: String,

    @ColumnInfo(name = "LATITUDE")
    @SerializedName("latitude")
    var latitude: String,

    @ColumnInfo(name = "LONGITUDE")
    @SerializedName("longitude")
    var longitude: String,

    @ColumnInfo(name = "ODO_METER")
    @SerializedName("odometer")
    var odometer: String,

    @ColumnInfo(name = "RPM")
    @SerialName("rpm")
    var rpm: String,

    @ColumnInfo(name = "SEQ")
    @SerializedName("seq")
    var seq: Int,

    @ColumnInfo(name = "VELOCITY")
    @SerializedName("velocity")
    var velocity: String,

    @ColumnInfo(name = "ODB2")
    @SerializedName("odb2")
    var odb2: String,

    @ColumnInfo(name = "ENGINE_TEMP")
    @SerialName("engineTemp")
    var engineTemp: String,

    @ColumnInfo(name = "SPEED")
    @SerialName("speed")
    var speed: String,

    @JsonIgnore
    @JsonProperty(value = "SYNCED")
    @ColumnInfo(name = "SYNCED")
    @SerializedName("synced")
    var synced: Boolean,

    @JsonIgnore
    @JsonProperty(value = "CHECKED")
    @ColumnInfo(name = "CHECKED")
    @SerializedName("checked")
    var checked: Boolean



) {
    @JsonIgnore
    @JsonProperty(value = "ID")
    @PrimaryKey(autoGenerate = true)
    var ID: Int = CURRENT_TIME_ID //**do not made it val**

    constructor() : this("",0,"", "","", "", "","","","","", "",-1, "", "", "", "",false, false)


}




