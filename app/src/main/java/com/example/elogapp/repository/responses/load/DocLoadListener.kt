package com.example.elogapp.repository.responses.load

interface DocLoadListener {

    fun onStarted()
    fun onSuccess(response: DocLoadsResponse, apiCode: Int)
    fun onFailure(message: String, apiCode: Int)
}