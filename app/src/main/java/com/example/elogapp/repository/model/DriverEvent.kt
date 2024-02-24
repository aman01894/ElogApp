package com.example.elogapp.repository.model

import kotlinx.serialization.SerialName

data class DriverEvent(

    @SerialName("id")
    var id: Int,

    @SerialName("userId")
    var userId: Int,

    @SerialName("driverName")
    var driverName: String,

    @SerialName("eventType")
    var eventType: Int,

    @SerialName("eventTime")
    var eventTime: String,

    @SerialName("location")
    var location: String,

    @SerialName("odoMeter")
    var odoMeter: Int,

    @SerialName("notes")
    var notes: String,

    @SerialName("cooDriverId")
    var coDriverId: Int,

    @SerialName("vehicleNo")
    var vehicleNo: Int,

    @SerialName("trailerNo")
    var trailerNo: Int,

    @SerialName("clientId")
    var clientId: Int
) {

    constructor() : this (-1, -1,"", -1,
        "", "",-1, "",-1, -1,-1,
        -1)
}