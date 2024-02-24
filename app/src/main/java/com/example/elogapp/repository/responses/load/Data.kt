package com.example.elogapp.repository.responses.load

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("id") val id: Int,
    @SerializedName("loadNo") val loadNo: String,
    @SerializedName("customerId") val customerId: Int,
    @SerializedName("customerRefrenceNumber") val customerRefrenceNumber: String,
    @SerializedName("dispatcher") val dispatcher: String,
    @SerializedName("loadEnteredBy") val loadEnteredBy: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("flatRate") val flatRate: Int,
    @SerializedName("extraP_D") val extraP_D: Int,
    @SerializedName("fuelSurcharge") val fuelSurcharge: Int,
    @SerializedName("extraCharge") val extraCharge: Int,
    @SerializedName("otherCharge") val otherCharge: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("carrier") val carrier: Int,
    @SerializedName("name") val name: String,
    @SerializedName("truck") val truck: String,
    @SerializedName("trailer") val trailer: String,
    @SerializedName("equipmentyType") val equipmentyType: String,
    @SerializedName("rate") val rate: Int,
    @SerializedName("otherCharges") val otherCharges: String,
    @SerializedName("totalMiles") val totalMiles: String,
    @SerializedName("status") var status: Int,
    @SerializedName("entityType") var entityType: Int,
    @SerializedName("customer") val customer: Customer,
    @SerializedName("paymentStatus") val paymentStatus: Int,
    @SerializedName("shippers") val shippers: List<Shippers>,
    @SerializedName("consignees") val consignees: List<Consignees>,
    @SerializedName("loadAdditionalPayments") val loadAdditionalPayments: List<LoadAdditionalPayments>
)