package com.example.elogapp.repository.responses.load

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName


data class DocGalleryInfo @Ignore constructor(


    @SerializedName("documentId")
    @ColumnInfo(name = "ROW_ID")
    var id: String,

    @SerializedName("loadId")
    @ColumnInfo(name = "LOAD_ID")
    var loadId: String,

    @SerializedName("image")
    @ColumnInfo(name = "IMG_PATH")
    var imgPath: String? = null,

    @ColumnInfo(name = "IMG_BASE_64")
    var displayName: String? = null,

    @ColumnInfo(name = "SAVED_DATE")
    var saveDate: String? = null,

    @ColumnInfo(name = "IS_SYNC")
    var isSync: Boolean,


    ) {
    constructor() : this("", "", "", "", "", true)

}

