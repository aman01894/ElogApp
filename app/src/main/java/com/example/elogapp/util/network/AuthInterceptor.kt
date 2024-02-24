package com.example.elogapp.util.network

import android.content.Context
import android.content.Intent
import com.example.elogapp.activity.auth.ReAuthPopupActivity
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.PreferenceHelper
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import okio.Buffer
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class AuthInterceptor(context: Context) : Interceptor {

    val mContext = context

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var pref = PreferenceHelper()
        val token = pref.getString(mContext, AppConstants.AUTH_KEY)

        var request = chain.request()
        val req = bodyToString(request)

        AppUtils.logger("http request: $req")

        try {
            val newRequest = request.newBuilder()

            newRequest.addHeader("Content-Type", "application/json")
            AppUtils.logger("http request: $token")
            AppUtils.logger("http request: $newRequest")


            if (token != null && token.isNotBlank()) {
                newRequest.addHeader("key", "$token")

                val deviceId = AppUtils.deviceId(mContext);
                newRequest.addHeader("DeviceId", deviceId)
            }
            val response = chain.proceed(newRequest.build())
            // Catching Responses From Retrofit
//            AppUtils.logger("http response onResponseIsSuccessful: "+response.isSuccessful)
//            AppUtils.logger("http response onResponseBody: "+response.body)
//            AppUtils.logger("http response onResponseMessage: "+response.message)
//            AppUtils.logger("http response onResponseCode: "+response.code)
//            AppUtils.logger("http response onResponseHeaders: "+response.headers)
            AppUtils.logger("http response OnResponse: $response")




            if (response.code == 401 ) {
                val modifiedRequest: Request?
                val client = OkHttpClient()

                val intent = Intent(mContext, ReAuthPopupActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                mContext.startActivity(intent)
            }

            return response

        } catch (e: Exception) {
            e.printStackTrace()
            var msg = ""
            var errCode:Int = 0
            when (e) {
                is SocketTimeoutException -> {
                    msg = "Timeout - Please try after some time"
                    errCode = AppConstants.ERROR_CODE_NO_INTERNET
                }
                is UnknownHostException -> {
                    msg = "Unable to make a connection. Please check your internet"
                    errCode = AppConstants.ERROR_CODE_NO_INTERNET
                }
                is ConnectionShutdownException -> {
                    msg = "Connection shutdown. Please check your internet"
                }
                is IOException -> {
                    msg = "Server is unreachable, please try again later."
                }
                is IllegalStateException -> {
                    msg = "${e.message}"
                }
                else -> {
                    msg = "${e.message}"
                }
            }

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(errCode)
                .message(msg)
                .body("{${e}}".toResponseBody(null)).build()

        }

    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "did not work"
        }
    }


}


