package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

const val CURRENT_ID_DRIVER_DUTY = 0

@Entity(tableName = "DRIVER_DUTY_INFO")
data class DriverDutyInfo @Ignore constructor(

    @ColumnInfo(name = "DUTY_ID")
    @SerialName("dutyId")
    var dutyId: Long,

    @ColumnInfo(name = "CLIENT_ID")
    @SerializedName("clientId")
    var clientId: Int,

    @ColumnInfo(name = "USER_ID")
    @SerializedName("userId")
    var userId: Int,

//    @ColumnInfo(name = "DRIVER_ID")
//    @SerialName("driverId")
//    var driverId: Int,

    @ColumnInfo(name = "DRIVER_NAME")
    @SerialName("driverName")
    var driverName: String?,


    @ColumnInfo(name = "CO_DRIVER_ID")
    @SerialName("coDriverId")
    var coDriverId: Int,

    @ColumnInfo(name = "CO_DRIVER_NAME")
    @SerialName("coDriverName")
    var coDriverName: String,

    @ColumnInfo(name = "VEHICLE_ID")
    @SerializedName("vehicleId")
    var vehicleId: Int,

//    @ColumnInfo(name = "TRUCK_ID")
//    @SerialName("truckId")
//    var vehicleId: Int,

    @ColumnInfo(name = "TRUCK_NUMBER")
    @SerialName("truckNumber")
    var vehicleNo: String,

    @ColumnInfo(name = "TRAILER_ID")
    @SerialName("trailerId")
    var trailerId: Int,

    @ColumnInfo(name = "TRAILER_NUMBER")
    @SerialName("trailerNumber")
    var trailerNumber: String,

    @ColumnInfo(name = "TRAILER_TYPE")
    @SerialName("trailerType")
    var trailerType: String,

    @ColumnInfo(name = "SLEEP_SPLIT")
    @SerialName("sleepSplit")
    var sleepSplit: String,


    @ColumnInfo(name = "CURRENT_LOCATION")
    @SerialName("currentLocation")
    var currentLocation: String,

    @ColumnInfo(name = "CUSTOM_LOCATION")
    @SerialName("customLocation")
    var customLocation: String,

    @ColumnInfo(name = "CURRENT_ODOMETER")
    @SerialName("currOdometer")
    var currentOdometer: String,

    @ColumnInfo(name = "NOTES")
    @SerialName("notes")
    var notes: String

    ){
    @PrimaryKey(autoGenerate = true)
    var id_primary_key: Int = CURRENT_ID_DRIVER_DUTY //**do not made it val**

    constructor () : this(-1, -1,-1,"",
        -1,"",-1,"",-1,"",
        "",
        "","","","","")



}