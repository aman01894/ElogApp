package com.example.elogapp.repository.responses.shippingdocs

import com.example.elogapp.database.ShippingDocs
import com.google.gson.annotations.SerializedName

data class ShippingDocResponse(

    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("exceptionMessage")
    val exceptionMessage: String,
    @SerializedName("data")
    val data: List<ShippingDocs>

)


