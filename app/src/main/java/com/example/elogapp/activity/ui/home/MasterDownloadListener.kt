package com.example.elogapp.activity.ui.home

import com.example.elogapp.repository.responses.BaseResponse
import com.example.elogapp.repository.responses.master.DropdownMasterResponse
import com.example.elogapp.repository.responses.master.MasterResponse

interface MasterDownloadListener {

    fun onStarted()
    suspend fun onSuccess(response: MasterResponse)
    suspend fun onSuccess(response: BaseResponse)
    suspend fun onSuccess(response: DropdownMasterResponse)
    fun onSuccess()
    suspend fun onFailure(message : String)

}