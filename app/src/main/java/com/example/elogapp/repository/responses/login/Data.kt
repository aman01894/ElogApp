package com.example.elogapp.repository.responses.login

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("id")
    val id: Int,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("userTypeId")
    val userTypeId: Int,
    @SerializedName("userType")
    val userType: String,
    @SerializedName("isActive")
    val isActive: Int,
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("masterDataVersion")
    val masterDataVersion: Int,
    @SerializedName("key")
    val key: String,
    @SerializedName("clientName")
    val clientNameShow: String


)