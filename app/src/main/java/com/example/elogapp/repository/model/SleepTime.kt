package com.example.elogapp.repository.model

data class SleepTime(
    var sleepList: ArrayList<Sleep>
) {
    data class Sleep(
        var id: Int,
        var sleepStartTime: Long,
        val splitSleepType: Int,
        var splitSleepTime : Long,
        var totalSleep : Long,
        var totalSplitSleep : Long,
        var eventType : String,

    )
}