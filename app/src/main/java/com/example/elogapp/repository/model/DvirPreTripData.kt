package com.example.elogapp.repository.model

import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class DvirPreTripData(

    @SerializedName("id") val id: Int,
    @SerializedName("driverId") val driverId: Int,
    @SerializedName("vehicleId") val vehicleId: Int,
    @SerializedName("vehiclHasDefect") val vehiclHasDefect: Int,
    @SerializedName("trailerId") val trailerId: String,
    @SerializedName("trailerHasDefect") val trailerHasDefect: String,
    @SerializedName("defect") val defect: String,
    @SerializedName("vehicleSatisfactory") val vehicleSatisfactory: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("clientId") val clientId: Int,
    @SerializedName("timeStamping") val timeStamping: String,
    //@SerializedName("client") val client : Client,
    //@SerializedName("driver") val driver : Driver,
    //@SerializedName("vehicle") val vehicle : Vehicle
)