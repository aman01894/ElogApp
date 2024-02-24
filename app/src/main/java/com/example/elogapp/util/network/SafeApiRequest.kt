package com.example.elogapp.util.network

import android.util.Log
import com.example.elogapp.util.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()
        if (response.isSuccessful) {

            val responseBody: T? = response.body()
            val content = responseBody!!.toString()
            Log.e("ELOG", "Api Response: $content")
            return response.body()!!


        } else {

            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {

                try {
                    message.append(JSONObject(it).getString("message"))

                } catch (e: JSONException) {
                }
                message.append("\n")
            }
                message.append("Error Code : ${response.code()}")

                throw ApiException(message.toString())

            }
        }


}






