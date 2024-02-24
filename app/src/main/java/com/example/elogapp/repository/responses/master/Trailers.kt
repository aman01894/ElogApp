package com.example.elogapp.repository.responses.master

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val CURRENT_ID_TRAILER = 0

@Entity(tableName = "TRAILER_MASTER")
data class Trailers(

		@ColumnInfo(name = "ID")
		@SerializedName("id")
		val id: Int,

		@ColumnInfo(name = "TRAILER_NUMBER")
		@SerializedName("trailerNumber")
		val trailerNumber: String,

		@ColumnInfo(name = "TRAILER_TYPE")
		@SerializedName("trailerType")
		val trailerType: String,

		@ColumnInfo(name = "CREATED_ON")
		@SerializedName("createdOn")
		val createdOn: String,

		@Embedded
		@SerializedName("createdBy")
		val createdBy: CreatedBy,

		@ColumnInfo(name = "IS_ACTIVE")
		@SerializedName("isActive")
		val isActive: Int
){

	@PrimaryKey(autoGenerate = true)
	var PRI_ID: Int = CURRENT_ID_TRAILER //**do not made it val**
}