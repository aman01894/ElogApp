package com.example.elogapp.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.elogapp.repository.responses.login.Permissions
import com.google.gson.annotations.SerializedName

const val CURRENT_USER_ID = 0

@Entity(tableName = "USER_DETAILS")
data class UserDetails(

        @ColumnInfo(name = "ID")
        @SerializedName("id")
        var id: Int,

        @ColumnInfo(name = "PARENT_USER_ID")
        @SerializedName("parentUserId")
        var parentUserId: Int,

        @ColumnInfo(name = "USER_EMAIL")
        @SerializedName("userEmail")
        var userEmail: String? = null,

        @ColumnInfo(name = "DISPLAY_NAME")
        @SerializedName("displayName")
        var displayName: String? = null,

        @ColumnInfo(name = "USER_TYPE_ID")
        @SerializedName("userTypeId")
        var userTypeId: Int,

        @ColumnInfo(name = "USER_TYPE")
        @SerializedName("userType")
        var userType: String? = null,

        @ColumnInfo(name = "IS_ACTIVE")
        @SerializedName("isActive")
        var isActive: Int,

        @ColumnInfo(name = "CLIENT_ID")
        @SerializedName("clientId")
        var clientId: Int,

        @ColumnInfo(name = "MASTER_DATA_VERSION")
        @SerializedName("masterDataVersion")
        var masterDataVersion: Int,

        @ColumnInfo(name = "KEY")
        @SerializedName("key")
        var key: String? = null,

        @ColumnInfo(name = "CLIENT_NAME_SHOW")
        @SerializedName("clientNameShow")
        var clientNameShow: String? = null,

        @SerializedName("timeZoneInfo")
        var timeZoneInfo: String? = null,

        @SerializedName("role")
        var role : String? = null,

        @SerializedName("deviceId")
        var deviceId : String? = null,

        @SerializedName("deviceDetails")
        var deviceDetails : String? = null,

        @SerializedName("applicationVersion")
        var applicationVersion : String? = null,

        @SerializedName("others")
        var others : String? = null,


//        @ColumnInfo(name = "PERMISSION")
//        @SerializedName("userPermissions")
//        var userPermissions : List<Permissions>



        @ColumnInfo(name = "FEATURE_ELD")
        @SerializedName("featureEld")
        var featureEld: Boolean = false,


        @ColumnInfo(name = "FEATURE_DISPATCH")
        @SerializedName("featureDispatch")
        var featureDispatch: Boolean = false



){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID //**do not made it val**




}

