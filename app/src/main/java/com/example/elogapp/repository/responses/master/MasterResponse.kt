package com.example.elogapp.repository.responses.master

import com.google.gson.annotations.SerializedName

data class MasterResponse(

		@SerializedName("status") val status : String,
		@SerializedName("message") val message : String,
		@SerializedName("exceptionMessage") val exceptionMessage : String,
		@SerializedName("data") val data : Data
)