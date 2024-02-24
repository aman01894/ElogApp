package com.example.elogapp.repository.responses.load

import com.google.gson.annotations.SerializedName

data class Customer(

    @SerializedName("id") val id : Int,
    @SerializedName("companyType") val companyType : Int,
    @SerializedName("customerName") val customerName : String,
    @SerializedName("dot") val dot : String,
    @SerializedName("mc") val mc : String,
    @SerializedName("taxId") val taxId : String,
    @SerializedName("billingAddress") val billingAddress : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : Int,
    @SerializedName("country") val country : String,
    @SerializedName("zipCode") val zipCode : String,
    @SerializedName("physicalAddress") val physicalAddress : String,
    @SerializedName("cityPhysical") val cityPhysical : String,
    @SerializedName("statePhysical") val statePhysical : Int,
    @SerializedName("countryPhysical") val countryPhysical : String,
    @SerializedName("zipCodePhysical") val zipCodePhysical : String,
    @SerializedName("contactEmail") val contactEmail : String,
    @SerializedName("dispatchEmail") val dispatchEmail : String,
    @SerializedName("dispatchPhone") val dispatchPhone : String,
    @SerializedName("alternateHoursPhone") val alternateHoursPhone : String,
    @SerializedName("accountingPhone") val accountingPhone : String,
    @SerializedName("accountingEmail") val accountingEmail : String,
    @SerializedName("insurranceProvider") val insurranceProvider : String,
    @SerializedName("expieryDate") val expieryDate : String,
    @SerializedName("noOfTrucks") val noOfTrucks : Int,
    @SerializedName("notes") val notes : String,
    @SerializedName("clientId") val clientId : Int,
//    @SerializedName("loadMasters") val loadMasters : List<String>
)