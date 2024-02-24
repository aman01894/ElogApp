package com.example.elogapp.repository.responses.master

import com.google.gson.annotations.SerializedName


data class Client(


//		@SerializedName("id")
	//@ColumnInfo(name = "ID")
//		val id : Int,

	@SerializedName("clientName")
	//@ColumnInfo(name = "CLIENT_NAME")
	val clientName: String,


	@SerializedName("logo")
	//@ColumnInfo(name = "LOGO")
	val logo: String,

	@SerializedName("address")
	//@ColumnInfo(name = "ADDRESS")
	val address: String,

	@SerializedName("contactNo")
	//@ColumnInfo(name = "CONTACT_NO")
	val contactNo: Int,

	@SerializedName("emailID")
	//@ColumnInfo(name = "EMAIL_ID")
	val emailID: String,

	@SerializedName("country")
	//@ColumnInfo(name = "COUNTRY")
	val country: Int,

	@SerializedName("state")
	//@ColumnInfo(name = "STATE")
	val state: Int,

	@SerializedName("zipCode")
	//@ColumnInfo(name = "ZIP_CODE")
	val zipCode: Int,

	@SerializedName("isActive")
	//@ColumnInfo(name = "IS_ACTIVE")
	val isActive: Int,

	//@TypeConverters(DataConverter::class)
	@SerializedName("users")
	//@ColumnInfo(name = "ADDRESS")
	val users: List<String>
)