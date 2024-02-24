package com.example.elogapp.activity.ui.loads.payment_history

import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.responses.load.LoadListener
import com.example.elogapp.repository.LoadRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException

class PaymentHistoryViewModel(private val repository: LoadRepository) : ViewModel() {

    var loadListener: LoadListener? = null

    fun getLoadData(fromDate: String, toDate: String) {

        loadListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getPaymentHistoryData(fromDate, toDate)
                authResponse?.let {
                    loadListener?.onSuccess(authResponse)
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


}