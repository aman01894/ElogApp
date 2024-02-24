package com.example.elogapp.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_ID_DD = 0

@Entity(tableName = "DROPDOWN_MASTER")
data class DropdownMaster(

    @SerializedName("code")
    @ColumnInfo(name = "CODE")
    val code: Int,
    @SerializedName("name")
    @ColumnInfo(name = "NAME")
    val name: String,
    @SerializedName("type")
    @ColumnInfo(name = "TYPE")
    val type: String

) {

    @PrimaryKey(autoGenerate = true)
    var PRI_ID: Int = CURRENT_ID_DD //**do not made it val**

}




