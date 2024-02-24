package com.example.elogapp.repository.responses.login

import com.google.gson.annotations.SerializedName

data class Client (

	@SerializedName("id")
	val id : Int,
	@SerializedName("clientName")
	val clientName : String,
	@SerializedName("logo")
	val logo : String,
	@SerializedName("address")
	val address : String,
	@SerializedName("contactNo")
	val contactNo : Int,
	@SerializedName("emailID")
	val emailID : String,
	@SerializedName("country")
	val country : Int,
	@SerializedName("state")
	val state : Int,
	@SerializedName("zipCode")
	val zipCode : Int,
	@SerializedName("masterDataVersion")
	val masterDataVersion : Int,
	@SerializedName("isActive")
	val isActive : Int,
	@SerializedName("users")
	val users : List<String>
)