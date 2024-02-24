package com.example.elogapp.activity.ui.loads.open_payment

import androidx.lifecycle.ViewModel
import com.example.elogapp.repository.responses.load.LoadListener
import com.example.elogapp.repository.LoadRepository
import com.example.elogapp.util.ApiException
import com.example.elogapp.util.Coroutines
import com.example.elogapp.util.NoInternetException

class OpenPaymentViewModel(private val repository: LoadRepository) : ViewModel() {

    var loadListener: LoadListener? = null

    fun getLoadData(status: Int) {

        loadListener?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getOpenLoadData(status)
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