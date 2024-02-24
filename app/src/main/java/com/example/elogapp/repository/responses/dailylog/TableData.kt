import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class TableData (

	@SerializedName("id") val id : Int,
	@SerializedName("userId") val userId : Int,
	@SerializedName("driverName") val driverName : String,
	@SerializedName("eventType") val eventType : Int,
	@SerializedName("eventName") val eventName : String,
	@SerializedName("duration") val duration : String,
	@SerializedName("eventTime") val eventTime : String,
	@SerializedName("eventEndTime") val eventEndTime : String,
	@SerializedName("location") val location : String,
	@SerializedName("customLocation") val customLocation : String,
	@SerializedName("odoMeter") val odoMeter : Int,
	@SerializedName("notes") val notes : String,
	@SerializedName("coDriverId") val coDriverId : String,
	@SerializedName("coDriverName") val coDriverName : String,
	@SerializedName("vehicleId") val vehicleId : Int,
	@SerializedName("vehicleNumber") val vehicleNumber : String,
	@SerializedName("trailerId") val trailerId : Int,
	@SerializedName("trailerNumber") val trailerNumber : String,
	@SerializedName("clientId") val clientId : Int,
	@SerializedName("isCertified") val isCertified : Int,
	@SerializedName("dutyId") val dutyId : String,
	@SerializedName("imageUrl") val imageUrl : String
)