package com.example.elogapp.activity.ui.dvir

import com.example.elogapp.repository.responses.dvir.NewDvirResponse

interface DvirListener {

    fun onStarted()
    fun onSuccess(user: NewDvirResponse)
    fun onFailure(message : String)
}