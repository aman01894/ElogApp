package com.example.elogapp.repository.responses.load

interface DocumentLoadListener {

    fun onStarted()
    fun onSuccess(response: LoadDispatchResponse)
    fun onFailure(message: String)
}