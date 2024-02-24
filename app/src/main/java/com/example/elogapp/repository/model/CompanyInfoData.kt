package com.example.elogapp.repository.model

import com.google.gson.annotations.SerializedName

data class CompanyInfoData(

    @SerializedName("id") val id: Int,
    @SerializedName("clientName") val clientName: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("address") val address: String,
    @SerializedName("contactNo") val contactNo: String,
    @SerializedName("emailID") val emailID: String,
    @SerializedName("country") val country: Int,
    @SerializedName("state") val state: Int,
    @SerializedName("zipCode") val zipCode: Int,
    @SerializedName("masterDataVersion") val masterDataVersion: Int,
    @SerializedName("isActive") val isActive: Int,
    @SerializedName("homeTerminal") val homeTerminal: String,
    @SerializedName("timeZone") val timeZone: String,
    @SerializedName("language") val language: String,
    @SerializedName("users") val users: List<String>


)

