package com.example.elogapp.activity.ui.dalily_log

import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.DailyLogRepository
import com.example.elogapp.repository.responses.dailylog.DailyLogListener
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import okhttp3.RequestBody

class DailyLogViewModel(private val repository: DailyLogRepository) : ViewModel() {

    var loadListener: DailyLogListener? = null

    fun getDailyLog(API_KEY: Int) {

        loadListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getDailyLog()
                authResponse?.let {
                    loadListener?.onSuccess(authResponse, API_KEY)
                    return@main
                }
                loadListener?.onFailure(authResponse.message!!)

            } catch (e: ApiException) {
                loadListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                loadListener?.onFailure(e.message!!)
            }
        }

    }

    fun sendCertificationDateToServer(event: RequestBody, API_KEY: Int){

        loadListener?.onStarted()
        Coroutines.main {
            try {
                val response = repository.sendCertificationDateToServer(event)
                response?.let {
                    loadListener?.onSuccess(response, API_KEY)
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