package com.example.elogapp.util.apilistner

interface ApiResponseListener {

    fun onStarted()
    fun onSuccess(obj: Any)
    fun onFailure(message : String)
}