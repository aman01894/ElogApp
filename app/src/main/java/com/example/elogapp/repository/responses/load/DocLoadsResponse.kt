package com.example.elogapp.repository.responses.load

import com.google.gson.annotations.SerializedName

data class DocLoadsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("exceptionMessage") val exceptionMessage: String,
    @SerializedName("data") val data: List<DocGalleryInfo>
)