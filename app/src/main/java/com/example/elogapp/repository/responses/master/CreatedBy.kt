package com.example.elogapp.repository.responses.master

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class CreatedBy(

//	@SerializedName("id")
//	val id: Int,
	@SerializedName("userName")
	val userName: String,
	@SerializedName("password")
	val password: Int,
	@SerializedName("userType")
	val userType: String?,

	@Embedded
	@SerializedName("client")
	val client: Client
)