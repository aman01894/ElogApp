package com.example.elogapp.repository.responses.shippingdocs

import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.elogapp.database.CURRENT_ID_DOCUMENT_GALLERY
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class ShippingDocsData (


    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "DOC_NO")
    @SerializedName("documentNumber")
    var docNumber: String,

    @ColumnInfo(name = "SAVED_DATE")
    @SerializedName("shippingDate")
    var saveDate: String?,

    @JsonIgnore
    @ColumnInfo(name = "IS_SYNC")
    @SerializedName("isSynced")
    var isSync: Boolean,


) {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = CURRENT_ID_DOCUMENT_GALLERY //**do not made it val**

    constructor() : this(0, "", "", false)
}