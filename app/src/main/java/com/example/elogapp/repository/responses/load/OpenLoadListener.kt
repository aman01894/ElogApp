package com.example.elogapp.repository.responses.load

interface OpenLoadListener {

    fun onStarted()
    fun onSuccess(response: LoadDispatchResponse)
    fun onFailure(message: String)
}