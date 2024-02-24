package com.example.elogapp.repository.responses.load

import com.google.gson.annotations.SerializedName

data class Consignees(

    @SerializedName("id") val id: Int,
    @SerializedName("loadId") val loadId: Int,
    @SerializedName("loadNumber") val loadNumber: String,
    @SerializedName("entityTypeId") val entityTypeId: Int,
    @SerializedName("shipperConsigneeId") val shipperConsigneeId: Int,
    @SerializedName("shipperConsigneeName") val shipperConsigneeName: String,
    @SerializedName("address") val address: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: Int,
    @SerializedName("stateName") val stateName: String,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("pickupNumber") val pickupNumber: String,
    @SerializedName("comodity") val comodity: String,
    @SerializedName("reeferTemp") val reeferTemp: String,
    @SerializedName("referModeId") val referModeId: Int,
    @SerializedName("caseCount") val caseCount: String,
    @SerializedName("pallets") val pallets: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("status") var status: Int,
    @SerializedName("shippingNotes") val shippingNotes: String,
    @SerializedName("contactNo") val contactNo: String,
    @SerializedName("contactPerson") val contactPerson: String
)