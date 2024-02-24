package com.example.elogapp.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.elogapp.database.EventLog
import com.example.elogapp.database.MyRoomDb
import com.example.elogapp.util.network.AuthInterceptor
import com.example.elogapp.util.network.MyApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class OutboundWorkManager(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val mData = MyRoomDb.instance?.getOutboundDao()?.getOutboundList(false)

        return@withContext runCatching {

            if (mData?.size!! > 0) {
                for(event in mData){

                    try {
                        val requestBody =
                            event.data?.toRequestBody()
                        AppUtils.logger("==>${event.data}")

                        if (!event.synced)
                        {
                        val br = MyApi.invoke(AuthInterceptor(applicationContext))
                            .sendEventDataToServer(requestBody!!)

                        val status = br.body()?.status
                            if (status != null) {
                                AppUtils.logger(br.body().toString())
                            }

                        status?.let {
                            if (status.equals("success",true))
                                MyRoomDb.instance?.getOutboundDao()?.update(event.ID, true)


                            AppUtils.deleteDataFromOutBound(event)
                        }

                    }
                    } catch (err: Exception) {
                        err.message?.let { AppUtils.sentryLoggerError(it) }
                    }
                }
            }else{

                AppUtils.logger("Outbound Manager no data to sync")
            }


            val mDataEvent = MyRoomDb.instance?.getEventLogDao()?.getAllEvents(false)

            if (mDataEvent?.size!! > 0) {

                for (event in mDataEvent) {

                    create(event)

                }
            }else{
                AppUtils.logger("Outbound Manager no Event data to sync")
            }

            AppUtils.logger("Outbound Work Manager status $Result.success()")
            Result.success()


        }.getOrElse {
            AppUtils.logger("Outbound Work Manager status $Result.failure()")
            Result.failure()
            }
        }




    private suspend fun create(event: EventLog) {

        val jsonInString: String = Gson().toJson(event)
        val json = JSONObject(jsonInString)

        val mainObj = JSONObject()
        mainObj.put(AppConstants.KEY_EVENTTYPE, AppConstants.EVENT_TYPE_DUTY)
        mainObj.put(AppConstants.KEY_EVENT_DATA, json)

        try {
            val requestBody = mainObj.toString()?.toRequestBody()
            AppUtils.logger("==>${mainObj}")

            if (!event.synced) {
                val br = MyApi.invoke(AuthInterceptor(applicationContext))
                    .sendEventDataToServer(requestBody!!)

                val status = br.body()?.status
                AppUtils.logger("HTTPS Status: $status")

                status?.let {
                    if (status.equals("success", true))
                        MyRoomDb.instance?.getEventLogDao()?.updateSyncStatus(event.ID, true)
                    MyRoomDb.instance?.getEventLogDao()?.delete(event)


                }

            }
        } catch (e: Exception) {
            e.message?.let { AppUtils.loggerError(it) }
        }

    }




}

