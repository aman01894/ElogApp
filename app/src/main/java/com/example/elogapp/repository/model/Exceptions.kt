package com.example.elogapp.repository.model

import androidx.room.ColumnInfo
import androidx.room.Ignore

data class Exceptions(

    @ColumnInfo(name = "NAME")
    var exceptionDetail: String,

    @ColumnInfo(name = "CODE")
    var exceptionId: Int,

    @Ignore
    var checked: Boolean,

) {

    constructor() : this ("", -1, false)
}
