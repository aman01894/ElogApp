package com.example.elogapp.repository.responses.master

import com.google.gson.annotations.SerializedName


data class DeviceTypeModel(

		//@SerializedName("id") val id: Int,
		@SerializedName("modelNumber") val modelNumber: String
)