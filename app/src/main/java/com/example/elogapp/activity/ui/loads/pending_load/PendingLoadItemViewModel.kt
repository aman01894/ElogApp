package com.example.elogapp.activity.ui.loads.pending_load

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.LoadRepository
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.LoadAcceptRejectListener
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class PendingLoadItemViewModel(private val repository: LoadRepository) : ViewModel() {

    var loadListener: LoadAcceptRejectListener? = null

    fun rejectAcceptLoad(mData: Data, statusType: Int, comment: String) {

        loadListener?.onStarted()
        Coroutines.main {
            try {

                val jObj =  JSONObject()
                jObj.put("loadId", mData.id)
                jObj.put("status", statusType)

                val requestBody = jObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
                Log.d("Request==>", jObj.toString())

                val loadResponse = repository.saveLoadAcceptReject(requestBody)
                loadResponse?.let {
                    loadListener?.onSuccess(loadResponse, statusType)
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