package com.example.elogapp.activity.ui.dvir_pretrip

import com.example.elogapp.repository.responses.predvir.PreDvirTripResponse

interface DvirPreTripListener {

    fun onStarted()
    fun onSuccess(user: PreDvirTripResponse)
    fun onFailure(message : String)
}