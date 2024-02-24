package com.example.elogapp.repository.responses.load

interface LoadListener {

    fun onStarted()
    fun onSuccess(response: LoadDispatchResponse)
    fun onFailure(message: String)
}