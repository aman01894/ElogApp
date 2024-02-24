package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName


const val CURRENT_ID_DOCUMENT_GALLERY = 0

@Entity(tableName = "DOCUMENT_GALLERY")
data class DocumentGallery @Ignore constructor(

        @ColumnInfo(name = "ROW_ID")
        @SerialName("rowId")
        var id: String,

        @ColumnInfo(name = "LOAD_ID")
        @SerialName("loadId")
        var loadId: String,

        @ColumnInfo(name = "IMG_PATH")
        @SerialName("imgPath")
        var imgPath: String? = null,

        @ColumnInfo(name = "IMG_BASE_64")
        @SerialName("imgBase64")
        var displayName: String? = null,

        @ColumnInfo(name = "SAVED_DATE")
        @SerialName("savedDate")
        var saveDate: String? = null,

        @ColumnInfo(name = "IS_SYNC")
        @SerialName("isSynced")
        var isSync: Boolean,


){

    @PrimaryKey(autoGenerate = true)
    var uid: Int = CURRENT_ID_DOCUMENT_GALLERY //**do not made it val**

        constructor() : this("", "", "", "", "", false)
}

