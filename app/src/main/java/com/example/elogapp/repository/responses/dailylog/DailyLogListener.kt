package com.example.elogapp.repository.responses.dailylog

interface DailyLogListener {

    fun onStarted()
    fun onSuccess(response: DailyLogResponse, API_KEY: Int)
    fun onSuccess(response: DailyLogChartResponse, API_KEY: Int)
    fun onFailure(message: String)
}