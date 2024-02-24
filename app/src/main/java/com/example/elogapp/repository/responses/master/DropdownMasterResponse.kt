package com.example.elogapp.repository.responses.master

import com.example.elogapp.database.DropdownMaster
import com.google.gson.annotations.SerializedName

data class DropdownMasterResponse(

		@SerializedName("status") val status : String,
		@SerializedName("message") val message : String,
		@SerializedName("exceptionMessage") val exceptionMessage : String,
		@SerializedName("data") val data : List<DropdownMaster>
)