package com.example.elogapp.repository.responses.master

import com.google.gson.annotations.SerializedName


data class Data(

        @SerializedName("user") val user: String,
        @SerializedName("devices") val devices: List<Device>,
//        @SerializedName("vehicles") val vehicles: List<Vehicle>,
//        @SerializedName("trailers") val trailers: List<Trailers>,
        @SerializedName("version") val version: Int
)