package com.example.elogapp.util.network

import com.example.elogapp.repository.responses.BaseResponse
import com.example.elogapp.repository.responses.companyinfo.CompanyInfoResponse
import com.example.elogapp.repository.responses.dailylog.DailyLogChartResponse
import com.example.elogapp.repository.responses.dailylog.DailyLogResponse
import com.example.elogapp.repository.responses.dvir.NewDvirResponse
import com.example.elogapp.repository.responses.load.DocLoadsResponse
import com.example.elogapp.repository.responses.load.LoadAcceptRejectResponse
import com.example.elogapp.repository.responses.load.LoadDispatchResponse
import com.example.elogapp.repository.responses.login.AuthResponse
import com.example.elogapp.repository.responses.master.DropdownMasterResponse
import com.example.elogapp.repository.responses.master.MasterResponse
import com.example.elogapp.repository.responses.predvir.PreDvirTripResponse
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocResponse
import com.example.elogapp.repository.responses.user.UserResponse
import com.example.elogapp.util.AppConstants
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {


    @FormUrlEncoded
    @POST("User/User")
    suspend fun getUser(
        @Field("id") id: String,
        @Field("key") key: String
    ): Response<UserResponse>

    @POST("User/SignIn")
    suspend fun userSignIn(@Body requestBody: RequestBody): Response<AuthResponse>

    @POST("Event/Save")
    suspend fun saveDriverEventNew(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("api/DataSync/Save")
    suspend fun sendEventDataToServer(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("Dvir/Save")
    suspend fun saveDvirEvent(@Body requestBody: RequestBody): Response<NewDvirResponse>

    @POST("Load/UpdateStatus")
    suspend fun updateAcceptRejectStatus(@Body requestBody: RequestBody): Response<LoadAcceptRejectResponse>

    @POST("Load/UpdateLoadDetailStatus")
    suspend fun updateLoadDetailStatus(@Body requestBody: RequestBody): Response<LoadAcceptRejectResponse>

    @POST("Load/DeleteDocument")
    suspend fun deleteDocImageFromServer(@Body requestBody: RequestBody): Response<DocLoadsResponse>

    @GET("Load/Documents")
    suspend fun getLoadDocuments(
        @Query("LoadId") loadId: Int
    ): Response<DocLoadsResponse>

    @GET("Load/GetDriverLoads")
    suspend fun getLoadData(
        @Query("status") status: Int
    ): Response<LoadDispatchResponse>


    @GET("Load/GetDriverOpenPayments")
    suspend fun getOpenLoadData(
        @Query("status") status: Int
    ): Response<LoadDispatchResponse>

    @GET("Load/GetPaymentHistory")
    suspend fun getPaymentHistoryData(
        @Query("from") fromDate: String,
        @Query("to") toDate: String
    ): Response<LoadDispatchResponse>


    @GET("Load/Loads")
    suspend fun getDocLoads(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
    ): Response<LoadDispatchResponse>

    @GET("DriverLog/GetDriverLogs")
    suspend fun getDailyLog(): Response<DailyLogResponse>

    @GET("DriverLog/GetChartLogs")
    suspend fun getDailyLogChart(
        @Query("eventDate") eventDate: String,
        @Query("driverId") driverId: Int,
    ): Response<DailyLogChartResponse>

    @POST("DriverLog/Certify")
    suspend fun sendCertificationDateToServer(@Body requestBody: RequestBody): Response<DailyLogResponse>


    @GET("Client/GetSingle")
    suspend fun getCustomerInfo(
        @Query("id") id: Int
    ): Response<CompanyInfoResponse>


    /**
     * Download Master Dropdown List
     */
    @GET("MasterData/DropDown")
    suspend fun getMasterDropdownData(
        @Query("UserId") userId: Int,
        @Query("ClientId") clientId: Int,
        @Query("sessionKey") key: String,
        @Query("Type") type: String
    ): Response<DropdownMasterResponse>

    /**
     * Pre Dvir List
     */
    @GET("Dvir")
    suspend fun getPreDvirListData(
        @Query("sessionKey") key: String
    ): Response<PreDvirTripResponse>


    /**
     * Get Shipping Doc List
     */
    @GET("Load/ShippingDocs")
    suspend fun getShippingDocList() : Response<ShippingDocResponse>


    @GET("MasterData")
    suspend fun getMasterData(
        @Query("UserId") userId: Int,
        @Query("ClientId") clientId: Int,
        @Query("sessionKey") key: String
    ): Response<MasterResponse>


    companion object {
        operator fun invoke(interceptor: AuthInterceptor): MyApi {

            val okHttpClient = OkHttpClient.Builder().
            addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(MyApi::class.java)

        }
    }


}