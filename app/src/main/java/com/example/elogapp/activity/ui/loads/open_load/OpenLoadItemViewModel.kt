package com.example.elogapp.activity.ui.loads.open_load

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.LoadRepository
import com.example.elogapp.repository.responses.load.LoadAcceptRejectListener
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class OpenLoadItemViewModel(private val repository: LoadRepository) : ViewModel() {

    var loadListener: LoadAcceptRejectListener? = null

    fun rejectAcceptLoad(obj: JSONObject) {

        loadListener?.onStarted()
        Coroutines.main {
            try {

                Log.d("Request==>", obj.toString())

                val requestBody = obj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                //Log.d("Request==>", Gson().toJson(obj).toString())


                val loadResponse = repository.updateLoadDetailStatus(requestBody)
                loadResponse?.let {
                    loadListener?.onSuccess(loadResponse, -1)
                    return@main
                }


            } catch (e: ApiException) {
                loadListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                loadListener?.onFailure(e.message!!)
            }
        }

    }


}