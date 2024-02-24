package com.example.elogapp.repository.responses.login

import com.google.gson.annotations.SerializedName

data class Permissions (

	@SerializedName("id")
	val id : Int,
	@SerializedName("userId")
	val userId : Int,
	@SerializedName("featureId")
	val featureId : Int,
	@SerializedName("featureName")
	val featureName : String,
	@SerializedName("view")
	val view : Int,
	@SerializedName("add")
	val add : Int,
	@SerializedName("edit")
	val edit : Int,
	@SerializedName("delete")
	val delete : Int,
	@SerializedName("export")
	val export : Int
)
	{
		constructor() : this(0,0,0,"",0,0,0,0,0)

	}

