package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val CURRENT_ID_SHIPPING_DOCUMENT = 0

@Entity(tableName = "SHIPPING_DOCUMENTS")
data class ShippingDocs @Ignore constructor(


        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id: Int,

        @ColumnInfo(name = "DOC_NO")
        @SerializedName("documentNumber")
        var documentNumber: String,

        @ColumnInfo(name = "SAVED_DATE")
        @SerializedName("shippingDate")
        var saveDate: String? = null,

        @ColumnInfo(name = "IS_SYNC")
        @SerializedName("isSynced")
        var isSync: Boolean,



){

    @PrimaryKey(autoGenerate = true)
    var uid: Int = CURRENT_ID_DOCUMENT_GALLERY //**do not made it val**

        constructor() : this(0, "", "", false)
}

