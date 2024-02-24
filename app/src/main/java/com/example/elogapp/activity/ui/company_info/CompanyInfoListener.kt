package com.example.elogapp.activity.ui.company_info

import com.example.elogapp.repository.responses.companyinfo.CompanyInfoResponse

interface CompanyInfoListener {

    fun onStarted()
    fun onSuccess(user: CompanyInfoResponse)
    fun onFailure(message : String)
}