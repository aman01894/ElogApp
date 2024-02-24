package com.example.elogapp.activity.ui.dalily_log

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.DailyLogRepository
import com.example.elogapp.repository.responses.dailylog.DailyLogListener
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException
import kotlinx.coroutines.*
import okhttp3.RequestBody

class DailyLogChartViewModel(private val repository: DailyLogRepository) : ViewModel() {

    var loadListener: DailyLogListener? = null

    fun getUserDetails (): LiveData<UserDetails> {

        return repository.getUserDetails()
    }

    fun getDailyLog(eventDate: String, driverId: Int, API_KEY: Int) {

        loadListener?.onStarted()
        viewModelScope.launch {
            try {
                val authResponse = repository.getDailyLogChart(eventDate,driverId)
                authResponse?.let {
                    loadListener?.onSuccess(authResponse, API_KEY)
                }
                //loadListener?.onFailure(authResponse.message!!)

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