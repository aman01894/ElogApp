package com.example.elogapp.repository.responses.master

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val CURRENT_ID = 0

@Entity(tableName = "DEVICE_MASTER")
data class Device(

        @SerializedName("id")
        @ColumnInfo(name = "ID")
        val id: Int,

        @SerializedName("deviceNumber")
        @ColumnInfo(name = "DEVICE_NUMBER")
        val deviceNumber: String,

        @SerializedName("mobileNumber")
        @ColumnInfo(name = "MOBILE_NUMBER")
        val mobileNumber: String,

        @SerializedName("client")
        @Embedded
        //@ColumnInfo(name = "CLIENT")
        val client: Client,

        @SerializedName("deviceTypeModel")
        @Embedded
        //@ColumnInfo(name = "DEVICE_TYPE_MODEL")
        val deviceTypeModel: DeviceTypeModel,

        @SerializedName("warrantyExpiry")
        @ColumnInfo(name = "WARRANTY_EXPIRY")
        val warrantyExpiry: String,

        @SerializedName("serviceExpiry")
        @ColumnInfo(name = "SERVICE_EXPIRY")
        val serviceExpiry: String,

        @SerializedName("billingDate")
        @ColumnInfo(name = "BILLING_DATE")
        val billingDate: String,

        @SerializedName("paymentStatus")
        @ColumnInfo(name = "PAYMENT_STATUS")
        val paymentStatus: Int,

        @SerializedName("isActive")
        @ColumnInfo(name = "IS_ACTIVE")
        val isActive: Int
){

        @PrimaryKey(autoGenerate = true)
        var PRI_ID: Int = CURRENT_ID //**do not made it val**

}