package com.example.elogapp.repository.responses.load

interface LoadAcceptRejectListener {

    fun onStarted()
    fun onSuccess(response: LoadAcceptRejectResponse, responseCode: Int)
    fun onFailure(message: String)
}