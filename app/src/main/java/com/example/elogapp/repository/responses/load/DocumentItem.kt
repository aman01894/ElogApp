package com.example.elogapp.repository.responses.load

import com.google.gson.annotations.SerializedName


data class DocumentItem(

    @SerializedName("loadId") val loadId: Int,
    @SerializedName("documentId") val documentId: String,
    @SerializedName("image") val imageUrl: String,

)