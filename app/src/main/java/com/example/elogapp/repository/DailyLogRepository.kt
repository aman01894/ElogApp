package com.example.elogapp.repository

import androidx.lifecycle.LiveData
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.database.UserDetails
import com.example.elogapp.repository.responses.dailylog.DailyLogChartResponse
import com.example.elogapp.repository.responses.dailylog.DailyLogResponse
import com.example.elogapp.util.network.MyApi
import com.example.elogapp.util.network.SafeApiRequest
import com.example.elogapp.repository.responses.load.LoadAcceptRejectResponse
import okhttp3.RequestBody

class DailyLogRepository(private val api: MyApi,
                         private val db: MyRoomDb
) : SafeApiRequest()  {

    suspend fun getDailyLog(): DailyLogResponse {

        return apiRequest { api.getDailyLog()}

    }

    fun getUserDetails (): LiveData<UserDetails> {

        return db.getUserDao().getUserDetails()
    }

    suspend fun getDailyLogChart(eventDate: String, driverId: Int): DailyLogChartResponse {

        return apiRequest { api.getDailyLogChart(eventDate,driverId)}

    }


    suspend fun sendCertificationDateToServer(event: RequestBody): DailyLogResponse {

        return apiRequest { api.sendCertificationDateToServer(event)}

    }

    suspend fun updateLoadDetailStatus(event: RequestBody): LoadAcceptRejectResponse {

        return apiRequest { api.updateLoadDetailStatus(event)}

    }


}