package com.example.elogapp.repository.responses.login

import com.google.gson.annotations.SerializedName

data class UserType (

		@SerializedName("id") val id : Int,
		@SerializedName("userType") val userType : Int,
		@SerializedName("userTypeName") val userTypeName : String
)